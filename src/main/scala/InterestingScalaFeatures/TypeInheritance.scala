package InterestingScalaFeatures

object TypeInheritance:
  trait Parent[T]:
    val current: T
    def next(): Parent[T]

  class Child[T] extends Parent[T]:
    override val current: T = ???
    override def next(): Child[T] = new Child[T]{
      override val current: T = ???

      override def next(): Child[T] = ???
    }
    def insert(e:T) = ()

  def main(args: Array[String]): Unit = {
    Child[String]().next().insert(null)
  }