val scala = "2.12.17"
val flink = "1.15.4"

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.mimikkk"
ThisBuild / scalaVersion := scala
ThisBuild / scalacOptions += "-target:jvm-1.8"
ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.4.0",
  "mysql" % "mysql-connector-java" % "8.0.32",
  "org.scala-lang" % "scala-library" % scala,
  "org.apache.flink" % "flink-core" % flink,
  "org.apache.flink" % "flink-clients" % flink,
  "org.apache.flink" %% "flink-scala" % flink,
  "org.apache.flink" %% "flink-streaming-scala" % flink,
  "org.apache.flink" % "flink-connector-kafka" % flink,
  "org.apache.flink" % "flink-connector-jdbc" % flink,
)
