ThisBuild / organization := "com.lsc"
ThisBuild / version := {
  val orig = (ThisBuild / version).value
  if (orig.endsWith("-SNAPSHOT")) "1.0.A-SNAPSHOT"
  else orig
}
ThisBuild / scalaVersion := "3.1.1"

val logbackVersion = "1.3.0-alpha10"
val sfl4sVersion = "2.0.0-alpha5"
val typesafeConfigVersion = "1.4.1"
val apacheCommonIOVersion = "2.11.0"
val scalacticVersion = "3.2.9"
val nscalatimeVersion = "2.30.0"
val apacheCommonMathVersion = "3.6.1"
val asmVersion = "9.2"
val guavaVersion = "30.1.1-jre"
val akkaVersion = "2.7.0"
val akkaHttpVersion = "10.5.0-M1"
val catsVersion = "2.8.0"
val catsEffectVersion = "3.3.14"
val snakeYamlVersion = "1.29"
val scalaZVersion = "7.4.0-M8"
val jsoupVersion = "1.14.3"
val codecVersion = "1.15"
val xmlVersion = "2.0.1"
val scalaReflectVersion = "2.13.8"
val scalaCompilerVersion = "2.13.8"
val https4sVersion = "0.23.6"

resolvers += ("Apache Snapshots" at "http://repository.apache.org/content/repositories/snapshots").withAllowInsecureProtocol(true)
resolvers += ("Apache repo" at "https://repository.apache.org/").withAllowInsecureProtocol(true)
resolvers += Resolver.sbtPluginRepo("releases")

//noinspection SpellCheckingInspection
lazy val root = (project in file("."))
  .settings(
    name := "PLANE",
    scalacOptions := Seq("-explain", "-Yexplain-lowlevel", "-Xfatal-warnings", "-unchecked", "-deprecation", "-feature", "-language:implicitConversions" ),
    scalacOptions += "-language:experimental.macros",
    description := "Programming Language ANalyses Experimentation",
    Test / parallelExecution := false,
    libraryDependencies ++= Seq(
//      ("com.typesafe.akka" %% "akka-actor-typed" % akkaVersion).cross(CrossVersion.for3Use2_13),
//      ("com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test).cross(CrossVersion.for3Use2_13),
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "ch.qos.logback" % "logback-core" % logbackVersion,
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "org.slf4j" % "slf4j-api" % sfl4sVersion,
      "com.typesafe" % "config" % typesafeConfigVersion,
      "commons-io" % "commons-io" % apacheCommonIOVersion,
      "org.apache.commons" % "commons-math3" % apacheCommonMathVersion,
      "org.apache.commons" % "commons-rng-simple" % "1.3",
      "org.typelevel" %% "cats-core" % catsVersion,
      "org.typelevel" %% "cats-laws" % catsVersion % Test,
      "org.typelevel" %% "cats-effect" % catsEffectVersion,
      "com.github.nscala-time" %% "nscala-time" % nscalatimeVersion,
      "org.scalactic" %% "scalactic" % scalacticVersion,
      "org.scalatest" %% "scalatest" % scalacticVersion % Test,
      "org.scalatest" %% "scalatest-featurespec" % scalacticVersion % Test,
      "org.scalaz" %% "scalaz-core" % scalaZVersion,
      "org.ow2.asm" % "asm" % asmVersion,
      "org.ow2.asm" % "asm-commons" % asmVersion,
      "org.ow2.asm" % "asm-util" % asmVersion,
      "com.google.guava" % "guava" % guavaVersion,
      "com.typesafe" % "config" % typesafeConfigVersion,
      "org.jsoup" % "jsoup" % jsoupVersion,
      "commons-codec" % "commons-codec" % codecVersion,
      "org.scala-lang.modules" %% "scala-xml" % xmlVersion,
      "org.yaml" % "snakeyaml" % snakeYamlVersion,
      "org.scala-lang" % "scala-reflect" % scalaReflectVersion,
      "org.scala-lang" % "scala-compiler" % scalaCompilerVersion,
      "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
      "org.http4s"      %% "http4s-ember-server" % https4sVersion,
      "org.http4s"      %% "http4s-ember-client" % https4sVersion,
      "org.http4s"      %% "http4s-circe"        % https4sVersion,
      "org.http4s"      %% "http4s-dsl"          % https4sVersion,
    ),
    homepage := Option(url("https://github.com/0x1DOCD00D/PLANE")),
    licenses := Seq("PLANE License" -> url("https://github.com/0x1DOCD00D/PLANE/LICENSE")),
    scriptedBufferLog := false,
    publishMavenStyle := false,
  )
