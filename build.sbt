name := "scalaz-amigo"
organization := "com.smartelk"
version := "0.1.0"
scalaVersion := "2.11.7"
scalacOptions := Seq("-deprecation", "-feature")
resolvers ++= Seq(
  "Maven central http" at "http://repo1.maven.org/maven2"
)
libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalameta" % "scalameta_2.11" % "0.0.5-M6",
  "org.scalameta" % "scalahost_2.11.7" % "0.0.5-M6",
  "org.scalatest" %%  "scalatest"   % "2.2.1" % "test",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.4" % "test"
)