package com.mimikkk.models.stockprice


import com.mimikkk.models.stockprice.StockPricePropertyExtensions.Timestamp
import org.apache.flink.api.common.eventtime.{TimestampAssignerSupplier, Watermark, WatermarkGeneratorSupplier, WatermarkOutput, TimestampAssigner => TimestampAssignerBase, WatermarkGenerator => WatermarkGeneratorBase, WatermarkStrategy => WatermarkStrategyBase}


final class StockPriceWatermarkStrategy extends WatermarkStrategyBase[StockPrice] {
  override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context) = new WatermarkGenerator()

  override def createTimestampAssigner(context: TimestampAssignerSupplier.Context) = new TimestampAssigner()

  private final class WatermarkGenerator extends WatermarkGeneratorBase[StockPrice] {
    private val maxOutOfOrderSeconds = 24 * 60 * 60 * 1000 // 24 hours
    private var maxTimestamp: Long = _

    override def onEvent(item: StockPrice, long: Long, output: WatermarkOutput): Unit = {
      maxTimestamp = math.max(item.timestamp, maxTimestamp)
    }

    override def onPeriodicEmit(watermarkOutput: WatermarkOutput): Unit = {
      watermarkOutput.emitWatermark(new Watermark(maxTimestamp - maxOutOfOrderSeconds - 1))
    }
  }

  private final class TimestampAssigner extends TimestampAssignerBase[StockPrice] {
    override def extractTimestamp(model: StockPrice, long: Long): Long = model.timestamp
  }
}

object StockPriceWatermarkStrategy {
  def create(): StockPriceWatermarkStrategy = new StockPriceWatermarkStrategy()
}
