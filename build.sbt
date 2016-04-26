name := "scalaz-amigo"
organization := "com.smartelk"
version := "0.0.1"
scalaVersion := "2.11.8"
scalacOptions := Seq("-deprecation", "-feature")
resolvers ++= Seq(
  "Maven central http" at "http://repo1.maven.org/maven2"
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalameta" %% "scalameta" % "0.1.0-RC2",
  "org.scalatest" %%  "scalatest"   % "2.2.1" % "test",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "org.scalaz" %% "scalaz-core" % "7.2.2" % "test"
)
