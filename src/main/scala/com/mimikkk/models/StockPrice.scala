package com.mimikkk.models

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
 * @param stockSymbol   Stock symbol
 */
final case class StockPrice
(
  date: Date,
  open: Float,
  high: Float,
  low: Float,
  close: Float,
  adjustedClose: Float,
  volume: Float,
  stockSymbol: String
)
