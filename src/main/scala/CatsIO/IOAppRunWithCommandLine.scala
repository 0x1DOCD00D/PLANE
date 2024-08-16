package CatsIO

import cats.effect.{ExitCode, IO, IOApp}

object IOAppRunWithCommandLine extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    if (args.headOption.contains("--filename"))
      IO.println("Command line option --filename specified").as(ExitCode.Success)
    else
      IO.println(args.mkString).as(ExitCode(-1))