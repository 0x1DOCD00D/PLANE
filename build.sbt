import sbt.Keys.scalaVersion

ThisBuild / organization := "com.lsc"

ThisBuild / version ~= { base =>
  if (base.endsWith("-SNAPSHOT")) "1.0.A-SNAPSHOT" else base
}


ThisBuild / scalaVersion := "3.7.1"

val logbackVersion = "1.5.7"
val typeSafeConfigVersion = "1.4.2"
val sfl4sVersion = "2.0.0-alpha5"
val typesafeConfigVersion = "1.4.1"
val apacheCommonIOVersion = "2.11.0"
val scalacticVersion = "3.2.9"
val nscalatimeVersion = "2.30.0"
val apacheCommonMathVersion = "3.6.1"
val asmVersion = "9.2"
val guavaVersion = "33.2.1-jre"
val akkaVersion = "2.10.0"
val akkaHttpVersion = sys.props.getOrElse("akka-http.version", "10.7.0")
val catsVersion = "2.13.0"
val catsEffectVersion = "3.6.3"
val log4catsV = "2.7.1"
val snakeYamlVersion = "2.0"
val scalaZVersion = "7.4.0-M8"
val codecVersion = "1.15"
val xmlVersion = "2.0.1"
val scalaReflectVersion = "2.13.8"
val scalaCompilerVersion = "2.13.8"
val https4sVersion = "0.23.6"
val monocleVersion = "3.1.0"
val scalaZversion = "7.3.8"
val stanfordNlpVersion = "4.5.7"
val pdfBoxVersion = "3.0.2"
val jsoupVersion = "1.17.2"
val langchain4jVersion = "0.33.0"
val ollama4jVersion = "1.0.73"
val jTokkitVersion = "1.1.0"
val deepLearning4jVersion = "1.0.0-M2.1"
val ShapelessVersion = "3.4.3"
val scalaFxVersion = "22.0.0-R33"
val spoonVersion = "11.1.0"
val scalaMetaVersion = "4.13.1.1"
val record4sVersion = "0.13.0"
val pureConfigVersion = "0.17.9"
val H2DatabaseVersion = "2.4.240"

resolvers += ("Apache Snapshots" at "http://repository.apache.org/content/repositories/snapshots")
  .withAllowInsecureProtocol(true)
resolvers += ("Apache repo" at "https://repository.apache.org/").withAllowInsecureProtocol(true)
resolvers += Resolver.sbtPluginRepo("releases")
Global / resolvers += "scala-integration" at
  "https://scala-ci.typesafe.com/artifactory/scala-integration/"
resolvers += "Akka library repository".at("https://repo.akka.io/maven")
resolvers += "Typesafe Repo" at "https://repo.typesafe.com/typesafe/releases/"

//noinspection SpellCheckingInspe
// ction
lazy val root = (project in file("."))
  .settings(
     name := "PLANE",
     scalacOptions := Seq(
        "-explain",
        "-Yexplain-lowlevel",
        "-Xprint:typer",
        "-Xignore-scala2-macros",
        "-unchecked",
        "-deprecation",
        "-feature",
        "-language:implicitConversions",
        "-source:3.3",
        "-experimental"
     ),
     scalacOptions += "-language:experimental.macros",
     javaOptions += "--add-opens=java.base/java.lang=ALL-UNNAMED",
     description := "Programming Language ANalyses Experimentation",
     Compile / run / fork := true,
     Compile / run / javaOptions += "--enable-preview",
     Test / javaOptions ++= Seq(
        "-Dscala.concurrent.context.numThreads=2",
        "-Dscala.concurrent.context.maxThreads=10",
        "--enable-preview"
     ),
     Test / fork := true,
     Test / parallelExecution := false,
     libraryDependencies ++= Seq(
        "com.typesafe" % "config" % typeSafeConfigVersion,
        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
        "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
        "com.typesafe.akka" %% "akka-stream" % akkaVersion,
        "com.typesafe.akka" %% "akka-pki" % akkaVersion,
        "ch.qos.logback" % "logback-core" % logbackVersion,
        "ch.qos.logback" % "logback-classic" % logbackVersion,
        "org.slf4j" % "slf4j-api" % sfl4sVersion,
        "commons-io" % "commons-io" % apacheCommonIOVersion,
        "org.apache.commons" % "commons-math3" % apacheCommonMathVersion,
        "org.apache.commons" % "commons-rng-simple" % "1.3",
        "org.typelevel" %% "cats-core" % catsVersion,
        "org.typelevel" %% "cats-laws" % catsVersion % Test,
        "org.typelevel" %% "cats-effect" % catsEffectVersion withSources() withJavadoc(),
        "org.typelevel" %% "log4cats-slf4j" % log4catsV,
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
        "org.scala-lang" %% "scala3-compiler" % scalaVersion.value,
        "org.scala-lang.modules" % "scala-parallel-collections_3" % "1.1.0",
        "org.http4s" %% "http4s-ember-server" % https4sVersion,
        "org.http4s" %% "http4s-ember-client" % https4sVersion,
        "org.http4s" %% "http4s-circe" % https4sVersion,
        "org.http4s" %% "http4s-dsl" % https4sVersion,
        "com.typesafe.akka" %% "akka-remote" % akkaVersion,
        "dev.optics" %% "monocle-core" % monocleVersion,
        "dev.optics" %% "monocle-macro" % monocleVersion,
        "org.scalaz" %% "scalaz-core" % scalaZversion,
        "edu.stanford.nlp" % "stanford-corenlp" % stanfordNlpVersion,
        "org.apache.pdfbox" % "pdfbox" % pdfBoxVersion,
        "org.jsoup" % "jsoup" % jsoupVersion,
        "dev.langchain4j" % "langchain4j" % langchain4jVersion,
        "io.github.amithkoujalgi" % "ollama4j" % ollama4jVersion,
        "com.knuddels" % "jtokkit" % jTokkitVersion,
        "org.deeplearning4j" % "deeplearning4j-core" % deepLearning4jVersion,
        "org.deeplearning4j" % "deeplearning4j-nlp" % deepLearning4jVersion,
        "org.nd4j" % "nd4j-native" % "1.0.0-M2.1",
        "io.github.ollama4j" % "ollama4j" % "1.0.84",
        "org.tensorflow" % "tensorflow" % "1.15.0",
        "org.scala-lang" %% "scala3-compiler" % scalaVersion.value,
        "org.typelevel" % "shapeless3-deriving_3" % ShapelessVersion,
        "eu.timepit" %% "refined" % "0.11.2",
        "org.scalafx" %% "scalafx" % scalaFxVersion,
        "org.scalameta" %% "scalameta" % scalaMetaVersion,
        "org.scala-lang" %% "scala3-tasty-inspector" % scalaVersion.value,
        "fr.inria.gforge.spoon" % "spoon-core" % spoonVersion,
        "com.github.tarao" %% "record4s" % record4sVersion,
       "com.github.pureconfig" %% "pureconfig-core" % pureConfigVersion,
       "com.github.pureconfig" %% "pureconfig-generic-scala3" % pureConfigVersion,
       "com.h2database" % "h2" % H2DatabaseVersion
     ),
     homepage := Option(url("https://github.com/0x1DOCD00D/PLANE")),
     licenses := Seq("PLANE License" -> url("https://github.com/0x1DOCD00D/PLANE/LICENSE")),
     scriptedBufferLog := false,
     publishMavenStyle := false
  )
