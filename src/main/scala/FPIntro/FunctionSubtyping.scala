package FPIntro

object FunctionSubtyping:
  class T1
  class T2
  class T3 extends T1
  class T4 extends T2

  class F1 extends (T1 => T2):
    override def apply(v1: T1): T2 = new T2

  class F2 extends (T3 => T2):
    override def apply(v1: T3): T2 = new T2
  
  class F3 extends (T1 => T4):
    override def apply(v1: T1): T4 = new T4


  def m1(o: F1): Unit =
    o.apply(new T1)
    println("ok m1")

  def m2(o: F2): Unit = println("ok m1")
  def m3(o: F3): Unit = println("ok m1")
  def m4(o: F3): Unit = println("ok m1")

  def main(args: Array[String]): Unit = {
    m1(new F1)
    m2(new F2)
    m4(new F3)
  }
