import sbt.Keys._
import sbt.Resolver

lazy val `scalaz-amigo` = (project in file(".")) settings(commonSettings: _*) settings (
  name := "scalaz-amigo",
  organization := "com.smartelk"
  ) dependsOn(scalahost % "compile->compile;test->test")


lazy val commonSettings = Seq(
  scalaVersion := "2.11.7",
  version := "0.1.0",
  resolvers ++= Seq("Maven central http" at "http://repo1.maven.org/maven2"),
  scalacOptions := Seq("-deprecation", "-feature")
)

lazy val scalaMetaCommonSettings = Defaults.coreDefaultSettings ++ Seq(
  resolvers += Resolver.sonatypeRepo("snapshots"),
  resolvers += Resolver.sonatypeRepo("releases"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full),
  libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "2.1.3" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test")
)

lazy val foundation = Project(
  id   = "foundation",
  base = file("scalameta/foundation")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's fundamental helpers and utilities",
  libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _ % "provided")
  )

lazy val artifacts = Project(
  id   = "artifacts",
  base = file("scalameta/scalameta/artifacts")
) settings (commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's APIs for reflecting artifacts of Scala ecosystem",
  libraryDependencies += "org.apache.ivy" % "ivy" % "2.4.0"
  ) dependsOn (foundation, exceptions, trees, parsers)

lazy val dialects = Project(
  id   = "dialects",
  base = file("scalameta/scalameta/dialects")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's dialects",
  libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _ % "provided")
  ) dependsOn (foundation, exceptions)

lazy val exceptions = Project(
  id   = "exceptions",
  base = file("scalameta/scalameta/exceptions")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's baseline exceptions"
  ) dependsOn (foundation)

lazy val interactive = Project(
  id   = "interactive",
  base = file("scalameta/scalameta/interactive")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's APIs for interactive environments (REPLs, IDEs, ...)"
  ) dependsOn (foundation, exceptions, artifacts)

lazy val parsers = Project(
  id   = "parsers",
  base = file("scalameta/scalameta/parsers")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's default implementation of the Parse[T] typeclass"
  ) dependsOn (foundation, exceptions, trees, tokens, tokenizers % "test", tql % "test")

lazy val prettyprinters = Project(
  id   = "prettyprinters",
  base = file("scalameta/scalameta/prettyprinters")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's baseline prettyprinters"
  ) dependsOn (foundation, exceptions)

lazy val quasiquotes = Project(
  id   = "quasiquotes",
  base = file("scalameta/scalameta/quasiquotes")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's quasiquotes for abstract syntax trees"
  ) dependsOn (foundation, exceptions, tokens, trees, parsers)

lazy val semantic = Project(
  id   = "semantic",
  base = file("scalameta/scalameta/semantic")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's APIs for traversing semantic structure of Scala programs"
  ) dependsOn (foundation, exceptions, prettyprinters, tokens, trees, artifacts)

lazy val tokenizers = Project(
  id   = "tokenizers",
  base = file("scalameta/scalameta/tokenizers")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's default implementation of the Tokenize typeclass",
  // TODO: This is a major embarassment: we need scalac's parser to parse xml literals,
  // because it was too hard to implement the xml literal parser from scratch.
  libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _)
  ) dependsOn (foundation, exceptions, tokens)

lazy val tokenquasiquotes = Project(
  id   = "tokenquasiquotes",
  base = file("scalameta/scalameta/tokenquasiquotes")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's quasiquotes for tokens",
  libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _ % "provided")
  ) dependsOn (foundation, exceptions, tokens, tokenizers)

lazy val tokens = Project(
  id   = "tokens",
  base = file("scalameta/scalameta/tokens")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's tokens and token-based abstractions (inputs and positions)",
  libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _ % "provided")
  ) dependsOn (foundation, exceptions, prettyprinters, dialects)

lazy val tql = Project(
  id   = "tql",
  base = file("scalameta/scalameta/tql")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's tree query language (basic and extended APIs)",
  libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _ % "provided")
  ) dependsOn (foundation, exceptions, trees)

lazy val trees = Project(
  id   = "trees",
  base = file("scalameta/scalameta/trees")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's abstract syntax trees",
  libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _ % "provided")
  ) dependsOn (foundation, exceptions, prettyprinters, tokens, tokenquasiquotes)

lazy val scalameta = Project(
  id   = "scalameta",
  base = file("scalameta/scalameta/scalameta")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  description := "Scala.meta's metaprogramming and hosting APIs"
  ) dependsOn (foundation, exceptions, dialects, interactive, parsers, prettyprinters, quasiquotes, semantic, artifacts, tokenizers, tokenquasiquotes, tql, trees)

lazy val scalahost = Project(
  id   = "scalahost",
  base = file("scalameta/scalahost")
) settings(commonSettings: _*) settings(scalaMetaCommonSettings: _*) settings(
  crossVersion := CrossVersion.full,
  description := "Scalac-based host that implements scala.meta's hosting APIs",
  libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _),
  scalacOptions in Test := {
    val defaultValue = (scalacOptions in Test).value
    val scalahostJar = (Keys.`package` in Compile).value
    System.setProperty("sbt.paths.scalahost.compile.jar", scalahostJar.getAbsolutePath)
    val addPlugin = "-Xplugin:" + scalahostJar.getAbsolutePath
    defaultValue ++ Seq("-Jdummy=" + scalahostJar.lastModified)
  }
  ) settings (
  ) dependsOn (scalameta)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalatest" %%  "scalatest"   % "2.2.1" % "test",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.4" % "test"
)