package FPIntro

// Regular Scala 3 version without capture checking

trait State[A]:
  def get: A
  def set(a: A): Unit

def get[A](using s: State[A]): A = s.get

def set[A](a: A)(using s: State[A]): Unit = s.set(a)

trait Rand:
  def range(min: Int, max: Int): Int

object rand:
  def fromState(using s: State[Long]): Rand =
    new Rand:
      override def range(min: Int, max: Int) =
        val seed = get
        val (nextSeed, next) = (seed + 1, seed.toInt) // obviously wrong, but not the point...
        set(nextSeed)
        next

@main def testSharedCapabilities(): Unit =
  println("SharedCapability example compiled successfully!")
  println("Using regular context parameters instead of capture checking!")
