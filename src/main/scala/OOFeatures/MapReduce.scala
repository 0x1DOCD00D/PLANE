package OOFeatures

object MapReduce {
  val lst = List(1, 3, 5, 8, 10)
  val lstStr = List("Sreten", "Alex", "Areeb","Krishna")

  def myOwnReduce(l: List[Int], e: Int, f: (v1:Int, v2:Int)=>Int ): Int =
    var sum: Int = e
    val len = lst.length
    var i: Int = 0
    while (i < len) {
      sum = f(sum, l(i))
      i = i + 1
    }
    sum

  def main(args: Array[String]): Unit = {
    var sum:Int = 0
    val len = lst.length
    var i:Int = 0
    while(i < len){
      sum = sum + lst(i)
      i = i + 1
    }

    val x = myOwnReduce(lst, 0, _+_)
    println(x)

    println(lst.reduce(_+_))
    println(lst.reduce(_-_))
    println(lst.reduce(_*_))
    println(lstStr.reduce((s1:String,s2:String)=>s1.concat(s2)))
  }
}
