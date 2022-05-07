package Cats

import cats.effect._
import cats.effect.unsafe.implicits.global

object BasicIO:
  @main def RunBasicIO(): Unit =
    val ioeff= IO { println("print me!") }

    val program =
      for {
        _ <- ioeff
        _ <- ioeff
      } yield ()

    program.unsafeRunSync()



