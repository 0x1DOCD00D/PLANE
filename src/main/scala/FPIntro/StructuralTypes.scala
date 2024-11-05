package FPIntro

object StructuralTypes:
  import scala.language.reflectiveCalls

  type CloseResource = { def close(): Boolean }

  class A:
    def close(): Boolean = true

  class B:
    def close(): Boolean = false

  def cleanUpResources(res: CloseResource)(f: ()=>Unit): Boolean =
    f()
    res.close()

  def main(args: Array[String]): Unit = {
    cleanUpResources(new A())(()=>println("A done!"))
    cleanUpResources(new B())(()=>println("B done!"))
  }