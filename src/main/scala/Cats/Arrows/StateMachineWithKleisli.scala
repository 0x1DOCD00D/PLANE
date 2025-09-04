package Cats.Arrows

object StateMachineWithKleisli {
  import cats.arrow.Arrow
  import cats.syntax.all.*

  // A simple Mealy machine
  final case class Machine[A, B](run: A => (Machine[A, B], B))

  object Machine:
    given Arrow[Machine] with
      override def id[A]: Machine[A, A] = Machine(a => (id, a))
      def lift[A, B](f: A => B): Machine[A, B] = Machine(a => (lift(f), f(a)))
      def compose[A, B, C](f: Machine[B, C], g: Machine[A, B]): Machine[A, C] =
        Machine(a => {
          val (g2, b) = g.run(a);
          val (f2, c) = f.run(b); (compose(f2, g2), c)
        })
      def first[A, B, C](fab: Machine[A, B]): Machine[(A, C), (B, C)] =
        Machine { case (a, c) =>
          val (fab2, b) = fab.run(a)
          (first(fab2), (b, c))
        }

  import Machine.given

  final case class Bucket(tokens: Double, lastMillis: Long)

  def tokenBucket(capacity: Double, ratePerSec: Double, st: Bucket): Machine[Long, Boolean] =
    Machine { now =>
      val elapsed = (now - st.lastMillis) / 1000.0
      val replen = (st.tokens + elapsed * ratePerSec).min(capacity)
      val allow = replen >= 1.0
      val nextTok = if allow then replen - 1.0 else replen
      val next = tokenBucket(capacity, ratePerSec, st.copy(tokens = nextTok, lastMillis = now))
      (next, allow)
    }

  // Helper identical to previous fanout
  def fanout[F[_, _] : Arrow, A, B, C](f: F[A, B], g: F[A, C]): F[A, (B, C)] =
    Arrow[F].lift((a: A) => (a, a)) >>> (f *** g)

  // Two independent buckets must both pass
  def dualLimiter(start: Long): Machine[Long, Boolean] =
    val global = tokenBucket(capacity = 100, ratePerSec = 50, Bucket(100, start))
    val user = tokenBucket(capacity = 10, ratePerSec = 5, Bucket(10, start))
    fanout(global, user) >>> Arrow[Machine].lift { case (gOk, uOk) => gOk && uOk }

  // Drive a machine over a stream of inputs
  def runAll[A, B](m: Machine[A, B], as: List[A]): List[B] =
    as match
      case h :: t => val (m2, b) = m.run(h); b :: runAll(m2, t)
      case _ => Nil

  // Example schedule (milliseconds)
  val t0 = 1_700_000_000_000L
  val inputs = List(0L, 100L, 200L, 250L, 1100L, 1200L).map(_ + t0)
  val results = runAll(dualLimiter(t0), inputs)
  // results: List[Boolean] = allow/deny decisions over time
  def main(args: Array[String]): Unit = {
    println(results)
  }
}
