package com.mimikkk.models.stockprice.anomaly

import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

final class StockPriceAnomalyProcessFunction extends
  ProcessWindowFunction[StockPriceAnomalyAggregator.Result, StockPriceAnomalyProcessFunction.Result, String, TimeWindow]
  with java.io.Serializable {
  override def process(stockId: String, context: Context, iterable: Iterable[StockPriceAnomalyAggregator.Result], collector: Collector[Result]): Unit =
    iterable foreach (anomaly => collector collect Result(
      context.window.getStart,
      context.window.getEnd,
      anomaly.stockId,
      anomaly.min,
      anomaly.max,
      anomaly.fluctuation
    ))

  private type Result = StockPriceAnomalyProcessFunction.Result
  private val Result = StockPriceAnomalyProcessFunction.Result
}

object StockPriceAnomalyProcessFunction {
  final case class Result(start: Long, end: Long, stockId: String, min: Float, max: Float, fluctuation: Float)
}
