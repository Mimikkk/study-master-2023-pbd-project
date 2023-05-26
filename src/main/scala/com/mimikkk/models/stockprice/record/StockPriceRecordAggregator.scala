package com.mimikkk.models.stockprice.record

import com.mimikkk.models.stockprice.StockPrice
import org.apache.flink.api.common.functions.AggregateFunction

final class StockPriceRecordAggregator extends AggregateFunction[StockPrice, StockPriceRecordAggregator.Accumulator, StockPriceRecordAggregator.Result] {
  override def createAccumulator(): Accumulator = Accumulator.empty

  override def add(item: StockPrice, accumulator: Accumulator): Accumulator = merge(Accumulator.from(item), accumulator)

  override def getResult(accumulator: Accumulator): Result = Result.from(accumulator)

  override def merge(first: Accumulator, second: Accumulator): Accumulator = new Accumulator(
    first.stockId,
    (first.close + second.close) / 2,
    math min(first.low, second.low),
    math max(first.high, second.high),
    first.volume + second.volume
  )

  private type Accumulator = StockPriceRecordAggregator.Accumulator
  private final val Accumulator = StockPriceRecordAggregator.Accumulator
  private type Result = StockPriceRecordAggregator.Result
  private final val Result = StockPriceRecordAggregator.Result
}

object StockPriceRecordAggregator {
  final case class Accumulator(stockId: String, close: Float, low: Float, high: Float, volume: Int)

  private object Accumulator {
    def empty = new Accumulator("", 0, 0, 0, 0)

    def from(item: StockPrice) = new Accumulator(item.stockId, item.close, item.low, item.high, item.volume)
  }

  final case class Result(stockId: String, close: Float, low: Float, high: Float, volume: Int)

  private object Result {
    def from(item: Accumulator) = new Result(item.stockId, item.close, item.low, item.high, item.volume)
  }
}
