ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.mimikkk"
ThisBuild / scalaVersion := "2.12.17"
ThisBuild / scalacOptions += "-target:jvm-1.8"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.4.0",
  "mysql" % "mysql-connector-java" % "8.0.32",
  "org.apache.flink" % "flink-core" % "1.16.1",
  "org.apache.flink" %% "flink-clients" % "1.14.4",
  "org.apache.flink" %% "flink-scala" % "1.16.1",
  "org.apache.flink" %% "flink-streaming-scala" % "1.16.1",
  "org.apache.flink" %% "flink-connector-kafka" % "1.14.4",
  "org.apache.flink" %% "flink-connector-jdbc" % "1.14.4"
)
