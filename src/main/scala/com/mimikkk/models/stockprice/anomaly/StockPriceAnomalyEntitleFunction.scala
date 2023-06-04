package com.mimikkk.models.stockprice.anomaly

import com.mimikkk.models.stockprice.StockMeta
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration

class StockPriceAnomalyEntitleFunction extends RichMapFunction[StockPriceAnomalyProcessFunction.Result, StockPriceAnomalyEntitleFunction.Result] {
  override def open(parameters: Configuration): Unit = {
    nameByStockIdMap = StockMeta.from(getRuntimeContext)
  }

  override def map(value: StockPriceAnomalyProcessFunction.Result): Result =
    Result(value.start, value.end, value.stockId, name(value.stockId), value.min, value.max, value.fluctuation)

  private var nameByStockIdMap: Map[String, String] = _

  private def name(stockId: String): String = nameByStockIdMap.getOrElse(stockId, null)

  private type Result = StockPriceAnomalyEntitleFunction.Result
  private val Result = StockPriceAnomalyEntitleFunction.Result
}

object StockPriceAnomalyEntitleFunction {
  final case class Result(start: Long, end: Long, stockId: String, title: String, min: Float, max: Float, fluctuation: Float)
}
