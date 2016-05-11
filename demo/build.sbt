name := "demo"
scalaVersion := "2.11.8"

//autoCompilerPlugins := true

scalacOptions += "-Xplugin:" + baseDirectory.value  + "/../target/scala-2.11/scalaz-amigo-assembly-0.0.1.jar"

scalacOptions ++= Seq(
  "-P:scalaz-amigo: alertMode: error",
  "-P:scalaz-amigo: turnOff: OptionUsage"
)

resolvers ++= Seq(
  "Maven central http" at "http://repo1.maven.org/maven2"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.2"
)
