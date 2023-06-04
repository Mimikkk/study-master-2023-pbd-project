package com.mimikkk.models.stockprice.record

import com.mimikkk.models.stockprice.StockMeta
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration

class StockPriceRecordEntitleFunction extends RichMapFunction[StockPriceRecordProcessFunction.Result, StockPriceRecordEntitleFunction.Result] {
  override def open(parameters: Configuration): Unit = {
    nameByStockIdMap = StockMeta.from(getRuntimeContext)
  }

  override def map(value: StockPriceRecordProcessFunction.Result): Result =
    Result(value.start, value.stockId, name(value.stockId), value.close, value.low, value.high, value.volume)

  private var nameByStockIdMap: Map[String, String] = _

  private def name(stockId: String): String = nameByStockIdMap.getOrElse(stockId, null)

  private type Result = StockPriceRecordEntitleFunction.Result
  private val Result = StockPriceRecordEntitleFunction.Result
}

object StockPriceRecordEntitleFunction {
  final case class Result(start: Long, stockId: String, title: String, close: Float, low: Float, high: Float, volume: Int)
}
