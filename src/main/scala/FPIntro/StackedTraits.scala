package FPIntro

object StackedTraits {
  abstract class IntQueue {

    def get(): Int

    def put(x: Int): Unit
  }

  import scala.collection.mutable.ArrayBuffer

  class BasicIntQueue extends IntQueue {
    private val buf = new ArrayBuffer[Int]

    def get() = buf.remove(0)

    def put(x: Int) =
      buf += x
  }

  trait Doubling extends IntQueue {
    abstract override def put(x: Int) =
      super.put(2 * x)
  }

  trait Incrementing extends IntQueue {
    abstract override def put(x: Int) =
      super.put(x + 1)

  }

  trait Filtering extends IntQueue {
    abstract override def put(x: Int) =
      if x >= 0 then super.put(x)

  }

  def main(args: Array[String]): Unit =
    println("stacked traits")
    val queue = new BasicIntQueue with Doubling with Incrementing with Filtering {}
    queue.put(-1)
    queue.put(3)
    queue.put(10)
    println(queue.get())
    println(queue.get())
    println(queue.get())

}
