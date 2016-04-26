name := "demo"
scalaVersion := "2.11.8"

autoCompilerPlugins := true

scalacOptions += "-Xplugin:../target/pack/lib/scalaz-amigo_2.11-0.0.1.jar"

resolvers ++= Seq(
  "Maven central http" at "http://repo1.maven.org/maven2"
)

libraryDependencies ++= Seq(
)
