scalaVersion := "2.11.7"
name := """akka-persistence-example"""
version := "1.0-SNAPSHOT"

PlayKeys.devSettings := Seq(
  "environment" -> "local",
  "cassandra.host" -> "127.0.0.1",
  "play.server.http.port" -> "9001")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases")
)

val AkkaVersion = "2.5.1"
val AkkaPersistenceCassandra = "0.54"

libraryDependencies ++= Seq(
  filters,
  "io.netty"                % "netty-transport-native-epoll"          % "4.0.41.Final",
  "com.typesafe.akka"       %% "akka-persistence"                     % AkkaVersion,
  "com.typesafe.akka"       %% "akka-persistence-cassandra"           % AkkaPersistenceCassandra,
  "com.google.protobuf"     % "protobuf-java-util"                    % "3.2.0",
  "com.typesafe.akka"       %% "akka-persistence-tck"                 % AkkaVersion               % Test,
  "com.typesafe.akka"       %% "akka-stream-testkit"                  % AkkaVersion               % Test,
  "com.typesafe.akka"       %% "akka-persistence-cassandra-launcher"  % AkkaPersistenceCassandra  % Test,
  "org.scalatestplus.play"  %% "scalatestplus-play"                   % "2.0.0"                   % Test,
  "org.scalamock"           %% "scalamock-scalatest-support"          % "3.5.0"                   % Test
)


lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)


/* Assembly Plugin */
assemblyJarName in assembly := "akka-persistence-example.jar"
mainClass in assembly := Some("play.core.server.ProdServerStart")
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "services", "com.fasterxml.jackson.databind.Module") => MergeStrategy.concat
  case PathList("META-INF", "services", xs @ _*) => MergeStrategy.deduplicate
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case "LICENSE" => MergeStrategy.discard
  case _ => MergeStrategy.first
}

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"
