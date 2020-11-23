name := "PLANE"

version := "0.1"

scalaVersion := "2.13.1"

scalacOptions ++= Seq(
  "-language:postfixOps",
  "-language:implicitConversions",
  "-feature",
  "-unchecked",
  "-deprecation",
  //  "-Xlint",
  "-Xfatal-warnings"
)

libraryDependencies ++= Seq("org.scalactic" %% "scalactic" % "3.1.1",
  "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  // https://mvnrepository.com/artifact/commons-codec/commons-codec
  "commons-codec" % "commons-codec" % "1.14",
  "com.github.nscala-time" %% "nscala-time" % "2.24.0",
  // https://mvnrepository.com/artifact/org.scalaz/scalaz-core
  "org.scalaz" %% "scalaz-core" % "7.4.0-M2",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.typelevel" %% "cats-core" % "2.0.0",
  "org.typelevel" %% "cats-free" % "2.0.0",
  "org.typelevel" %% "simulacrum" % "1.0.0",
  // https://mvnrepository.com/artifact/org.ow2.asm/asm-debug-all
  "org.ow2.asm" % "asm-debug-all" % "5.2",
  "org.scalactic" %% "scalactic" % "3.2.0",
  // https://mvnrepository.com/artifact/org.scalatest/scalatest
  "org.scalatest" %% "scalatest" % "3.2.0" % "test",
  //  "org.mozilla" % "rhino" % "1.7.13"
  // https://mvnrepository.com/artifact/org.jsoup/jsoup
  "org.jsoup" % "jsoup" % "1.13.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0")

resolvers += Resolver.sbtPluginRepo("releases")


val catsVersion = "1.0.1"
val catsCore = "org.typelevel" %% "cats-core" % catsVersion

lazy val root = (project in file(".")).
  settings(
    scalacOptions ++= Seq(
      "-deprecation",
      "-Ymacro-annotations",
      "-encoding", "UTF-8",
      "-feature",
      "-Xcheckinit",
      "-language:_"
    )
  )