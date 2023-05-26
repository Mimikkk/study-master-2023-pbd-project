package com.mimikkk.models.stockprice.record

import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

final class StockPriceRecordProcessFunction
  extends ProcessWindowFunction[StockPriceRecordAggregator.Result, StockPriceRecordProcessFunction.Result, String, TimeWindow] {
  override def process(stockId: String, context: Context, iterable: Iterable[StockPriceRecordAggregator.Result], collector: Collector[Result]): Unit =
    iterable foreach (record => collector collect Result(
      context.window.getStart,
      record.stockId,
      record.close,
      record.low,
      record.high,
      record.volume
    ))

  private type Result = StockPriceRecordProcessFunction.Result
  private val Result = StockPriceRecordProcessFunction.Result
}

object StockPriceRecordProcessFunction {
  final case class Result(start: Long, stockId: String, close: Float, low: Float, high: Float, volume: Int)
}
