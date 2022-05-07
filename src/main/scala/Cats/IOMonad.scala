package Cats

import scala.io.StdIn

object IOMonad:
  case class IO_Monad[T](thunk: () => T):
    def unit(value: => T): IO_Monad[T] = IO_Monad(()=>value)
    def map[S](f: T => S): IO_Monad[S] =
      println("map: " + f.toString())
      IO_Monad(() => f(thunk()))
    def flatMap[S](f: T => IO_Monad[S]): IO_Monad[S] =
      println("flatMap: " + f.toString())
      //passing f as a => some mapping expressions
      //and since f is applied to thunk()
      //it means that the result of thunk() is substituted for a
      IO_Monad(() => f(thunk()).thunk())
  end IO_Monad

  def ReadOneLine(): IO_Monad[String] = IO_Monad(StdIn.readLine)

  def PrintOneLine(data: Int): IO_Monad[Unit] = IO_Monad(() => println(data))

  def ReturnInt(): IO_Monad[Int] = IO_Monad(() => 1)

  def PrintOneLine(data: String): IO_Monad[Unit] = IO_Monad(() => println(data))

  @main def doIoMonad(): Unit =
    //T is void since it is println(...)
    //Hence IO_Monad(thunk: ()=>void)
    //a becomes this empty function content, void
    PrintOneLine("Testing...").flatMap(a =>
      println(a.getClass.getTypeName)
        IO_Monad (() => println("blah..."))).thunk()

    ReturnInt().flatMap(a =>
        println(a.getClass.getTypeName + ", "+ a.toString)
        IO_Monad (() => println("blah..."))).thunk()

    val program: IO_Monad[Unit] = for {
      _ <- PrintOneLine("Enter an integer: ")
      data <- ReadOneLine().map(_.toInt)
      _ <- PrintOneLine(data)
    } yield ()
    program.thunk()
