ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.mimikkk"
ThisBuild / scalaVersion := "2.11.12"
ThisBuild / scalacOptions += "-target:jvm-1.8"

val flinkVersion = "1.16.1"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.4.0",
  "mysql" % "mysql-connector-java" % "8.0.32",
  "org.apache.flink" % "flink-core" % flinkVersion,
  "org.apache.flink" %% "flink-clients" % flinkVersion,
  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.apache.flink" %% "flink-connector-kafka" % flinkVersion,
  "org.apache.flink" %% "flink-connector-jdbc" % flinkVersion
)
