package Givens

import java.util
import java.util.*

object AddMap2ArrayList:
  trait Functor[F[_]]:
    def map[S,T](arr:F[S])(f: S => T): F[T]

/*
  val functor4ArrayList = new Functor[ArrayList] {
    override def map[S, T](arr: ArrayList[S])(f: S => T): ArrayList[T] =
      val dest = new ArrayList[T]()
      for( elemIndex <- 0 until arr.size() ) {
        dest.add(f(arr.get(elemIndex)))
      }
      dest
  }
*/

  given functor4ArrayList:Functor[util.ArrayList] with {
    override def map[S, T](arr: util.ArrayList[S])(f: S => T): util.ArrayList[T] =
      val dest = new util.ArrayList[T]()
      for( elemIndex <- 0 until arr.size() ) {
        dest.add(f(arr.get(elemIndex)))
      }
      dest
  }

  extension [A] (list: util.ArrayList[A])(using functor4ArrayList:Functor[util.ArrayList])
    def map[B](f: A => B): util.ArrayList[B] = functor4ArrayList.map(list)(f)


  @main def runAddMap(): Unit =
    val al = new util.ArrayList[String]()
    al.add("howdy")
    al.add("dowdy!!")
    val res = al.map(_.length)
    println(res.toArray.mkString(","))
