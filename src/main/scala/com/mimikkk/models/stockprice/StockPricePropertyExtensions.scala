package com.mimikkk.models.stockprice

object StockPricePropertyExtensions {
  implicit class Timestamp(val self: StockPrice) {
    def timestamp: Long = self.date.getTime
  }
}
