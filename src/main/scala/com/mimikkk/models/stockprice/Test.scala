package com.mimikkk.models.stockprice

import java.text.SimpleDateFormat
import java.util.Date

/** Stock Market Price during one day of trading
 *
 * @param date          Specifies trading date
 * @param open          Opening price
 * @param high          Maximum price during the day
 * @param low           Minimum price during the day
 * @param close         Close price adjusted for splits
 * @param adjustedClose Adjusted close price adjusted for both dividends and splits
 * @param volume        The number of shares that changed hands during a given day
 * @param stockId       Identifier of a stock symbol
 */

object Test extends App {
  private final val format = new SimpleDateFormat("yyyy-MM-dd")
  val x = "1962-06-28T00:00:00.000Z,0.0,230.0,225.625,230.0,105.21490478515625,1100.0,NAV"
  println(x.split(",").mkString("Array(", ", ", ")"))
  val xs = x.split(",")
  println(format.parse(xs(0)))

}
