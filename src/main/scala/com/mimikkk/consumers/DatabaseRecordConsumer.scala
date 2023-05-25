package com.mimikkk.consumers

import java.sql.{Connection, Date, DriverManager}

object DatabaseRecordConsumer extends RecordConsumer {
  if (args.length != 3) {
    println("Usage: DatabaseRecordConsumer <database_url: database-url> <username: string> <password: string>")
    System.exit(1)
  }
  private final val url = args(0)
  private final val username = args(1)
  private final val password = args(2)
  private final val selectRecords = "SELECT * FROM stock_prices ORDER BY window_start DESC LIMIT 50"

  private final var connection: Connection = _
  try {
    Class forName "com.mysql.cj.jdbc.Driver"
    connection = DriverManager.getConnection(url, username, password)
    val statement = connection.createStatement

    while (true) {
      val records = statement executeQuery selectRecords

      println("< Stream Found >")
      while (records.next) {
        val start = new Date(records getLong "window_start").toLocalDate
        val end = start plusDays 30

        val stockPriceId = records getString "id"
        val stockSymbol = records getString "symbol"

        val from = s"${start.getYear}-${start.getMonth}-${start.getDayOfMonth}"
        val to = s"${end.getYear}-${end.getMonth}-${end.getDayOfMonth}"

        println(s"$from - $to \t $stockSymbol($stockPriceId)")
      }

      Thread.sleep(1000)
    }
  } catch {
    case e: Exception => e printStackTrace()
  }
  connection close()
}
