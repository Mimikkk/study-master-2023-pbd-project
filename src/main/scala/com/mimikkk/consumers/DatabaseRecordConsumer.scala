package com.mimikkk.consumers

import java.sql.{Connection, Date, DriverManager}
import com.mysql.cj.jdbc.Driver

object DatabaseRecordConsumer extends RecordConsumer {
  if (args.length != 3) {
    println(
      """
        |Usage of DatabaseRecordConsumer:
        |  <database_url: url-string>
        |  <username: string>
        |  <password: string>
      """.stripMargin
    )
    System.exit(1)
  }
  private final val url = args(0)
  private final val username = args(1)
  private final val password = args(2)
  private final val selectRecords = "SELECT * FROM stock_prices ORDER BY window_start DESC LIMIT 50"


  private final var connection: Connection = _
  try {
    DriverManager.registerDriver(new Driver)
    connection = DriverManager.getConnection(url, username, password)
    val statement = connection.createStatement

    while (true) {
      val records = statement executeQuery selectRecords

      println("< Stream Found >")
      while (records.next) {
        val start = new Date(records getLong "window_start").toLocalDate
        val end = start plusDays 30

        val stockPriceId = records getString "stock_id"
        val stock = records getString "title"

        val from = s"${start.getYear}-${start.getMonth}-${start.getDayOfMonth}"
        val to = s"${end.getYear}-${end.getMonth}-${end.getDayOfMonth}"

        val close = records getFloat "close"
        val low = records getFloat "low"
        val high = records getFloat "high"
        val volume = records getInt "volume"

        println(s"$from - $to \t $stock(id=$stockPriceId, close=$close, low=$low, high=$high, volume==$volume)")
      }

      Thread.sleep(1000)
    }
  } catch {
    case e: Exception => e printStackTrace()
  }
  connection close()
}

