package FPIntro

object DeadlockedLaziness:
  private lazy val x: Int = this.synchronized {
    println("Initializing x")
    y
  }

  private lazy val y: Int = this.synchronized {
    println("Initializing y")
    x
    100
  }

  def accessX(): Unit = this.synchronized {
    x // Access `x` within a synchronized block
    y
    ()
  }

  def accessY(): Unit = this.synchronized {
    y // Access `y` within a synchronized block
    x
    ()
  }

  def main(args: Array[String]): Unit = {
    val t1 = new Thread(() => accessX())
    val t2 = new Thread(() => accessY())

    t1.start()
    t2.start()

    t1.join()
    t2.join()

  }