package scala3Features.Context

trait Logger { def info(msg: String): Unit }

given Logger = msg => println(s"[info] $msg")

def log(msg: String)(using L: Logger): Unit =
  L.info(msg)

def withLogger[A](work: Logger ?=> A): A = work(using summon[Logger])

object LoggingDemo:

  def program: Logger ?=> Unit =
    log("starting program")
    log("doing work")
    log("done")

  @main def runLogging(): Unit =
    withLogger { program }