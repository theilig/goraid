name := "GoRaid"

version := "1.0"

lazy val `goraid` = (project in file(".")).enablePlugins(PlayScala).enablePlugins(DockerPlugin)


resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq( ehcache , ws , specs2 % Test , guice )

libraryDependencies ++= Seq("mysql" % "mysql-connector-java" % "8.0.28")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"
)

libraryDependencies += "com.typesafe.play" %% "play-mailer" % "8.0.1"

libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "8.0.1"

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.4"

libraryDependencies += "com.nimbusds" % "nimbus-jose-jwt" % "9.21"

libraryDependencies += "com.sendgrid" % "sendgrid-java" % "4.9.0"

libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.2"

libraryDependencies += specs2 % Test

unmanagedResourceDirectories in Test +=  baseDirectory ( _ /"target/web/public/test" ).value

daemonUser in Docker    := "goraid"
