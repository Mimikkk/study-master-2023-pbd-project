val scala = "2.12.17"
val flink = "1.15.4"

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.mimikkk"
ThisBuild / scalaVersion := scala
ThisBuild / scalacOptions += "-target:jvm-1.8"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.4.0",
  "mysql" % "mysql-connector-java" % "8.0.32",
  "org.scala-lang" % "scala-library" % scala % "provider",
  "org.apache.flink" % "flink-core" % flink % "provided",
  "org.apache.flink" % "flink-clients" % flink % "provided",
  "org.apache.flink" %% "flink-scala" % flink % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flink % "provided",
  "org.apache.flink" % "flink-connector-kafka" % flink % "provided",
  "org.apache.flink" % "flink-connector-jdbc" % flink % "provided",
)
