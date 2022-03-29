logLevel := Level.Warn

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.13")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.9")
