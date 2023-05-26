package com.mimikkk.models.stockprice.anomaly

import com.mimikkk.models.stockprice.StockPrice
import org.apache.flink.api.common.functions.AggregateFunction

final class StockPriceAnomalyAggregator extends AggregateFunction[StockPrice, StockPriceAnomalyAggregator.Accumulator, StockPriceAnomalyAggregator.Result] {
  override def createAccumulator(): Accumulator = Accumulator.empty

  override def add(item: StockPrice, accumulator: Accumulator): Accumulator = merge(Accumulator.from(item), accumulator)

  override def getResult(accumulator: Accumulator): Result = Result.from(accumulator)

  override def merge(first: Accumulator, second: Accumulator): Accumulator = new Accumulator(
    first.stockId,
    math.min(first.min, second.min),
    math.max(first.max, second.max)
  )

  private type Accumulator = StockPriceAnomalyAggregator.Accumulator
  private final val Accumulator = StockPriceAnomalyAggregator.Accumulator
  private type Result = StockPriceAnomalyAggregator.Result
  private final val Result = StockPriceAnomalyAggregator.Result
}

object StockPriceAnomalyAggregator {
  final case class Accumulator(stockId: String, min: Float, max: Float)

  private object Accumulator {
    def empty = new Accumulator("", 0, 0)

    def from(item: StockPrice) = new Accumulator(item.stockId, item.low, item.high)
  }

  final case class Result(stockId: String, min: Float, max: Float, fluctuation: Float)

  private object Result {
    def from(item: Accumulator) =
      new Result(item.stockId, item.min, item.max, (item.max - item.min) / item.max)
  }
}
