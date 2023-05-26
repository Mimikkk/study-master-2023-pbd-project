package com.mimikkk.sinks

import org.apache.flink.connector.jdbc.{JdbcConnectionOptions, JdbcExecutionOptions, JdbcSink, JdbcStatementBuilder}
import org.apache.flink.streaming.api.functions.sink.SinkFunction

object DatabaseSinkFactory {
  def create[T]
  (
    statement: String,
    statementBuilder: JdbcStatementBuilder[T],
    url: String,
    username: String,
    password: String
  ): SinkFunction[T] = JdbcSink.sink(
    statement,
    statementBuilder,
    JdbcExecutionOptions.builder()
      .withBatchSize(batchSize)
      .withBatchIntervalMs(intervalMilliseconds)
      .withMaxRetries(maxRetries)
      .build(),
    new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
      .withUrl(url)
      .withDriverName(driver)
      .withUsername(username)
      .withPassword(password)
      .build()
  )

  private val driver: String = "com.mysql.cj.jdbc.Driver"
  private val batchSize: Int = 128
  private val intervalMilliseconds: Int = 100
  private val maxRetries: Int = 3
}
