package scala3Features.NewTypes

//https://github.com/scala/scala3/pull/23821

object SingleLineLambda extends App {
  def delay(ms: Double)(fn: => Unit): Unit =
    Thread.sleep(ms.toLong)
    fn

  delay(300):
    println("hello")

  {
    def delay(ms: Double)(fn: => Unit): Unit =
      Thread.sleep(ms.toLong)
      fn

    delay(300):
      println("hello")
  }

  {
    def delay(ms: Double)(fn: => Unit): Unit =
      Thread.sleep(ms.toLong)
      fn

    delay(300):
      println(s"time $timestamp")
  }
def timestamp = System.currentTimeMillis()
}
