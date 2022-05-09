package Cats

import cats.effect.IO

object Aid4Debugging:
  def putStrLn[T](value: T): IO[Unit] = IO(println(value.toString))

  def printStackContent: IO[Unit] = IO(Thread.currentThread().getStackTrace.foreach(println))

  def printStackContentEagerly: Unit = Thread.currentThread().getStackTrace.foreach(println)

  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log
  
  extension [A](io: IO[A])
    def showThreadAndData: IO[A] = 
      io.map(value => {
        val x = 1
        println(value)
      })
      for {
      someValue <- io
      t = (Thread.currentThread().getName, Thread.currentThread().getId)
      _ = println(s"Thread [${t._1}, ${t._2}] ${someValue.toString}")
    } yield someValue