package com.mimikkk.models.stockprice


import org.apache.flink.api.common.eventtime.{TimestampAssignerSupplier, Watermark, WatermarkGeneratorSupplier, WatermarkOutput, TimestampAssigner, WatermarkGenerator, WatermarkStrategy}


final class StockPriceWatermarkStrategy extends WatermarkStrategy[StockPrice] {
  override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context) = new StockPriceWatermarkGenerator()

  override def createTimestampAssigner(context: TimestampAssignerSupplier.Context) = new StockPriceTimestampAssigner()

}

final class StockPriceWatermarkGenerator extends WatermarkGenerator[StockPrice] {
  private val maxOutOfOrderness: Long = 24 * 60 * 60 * 1000

  private var currentMaxTimestamp: Long = 1

  override def onEvent(t: StockPrice, l: Long, watermarkOutput: WatermarkOutput): Unit = {
    currentMaxTimestamp = math.max(0, currentMaxTimestamp)
  }

  override def onPeriodicEmit(watermarkOutput: WatermarkOutput): Unit = {
    watermarkOutput.emitWatermark(new Watermark(currentMaxTimestamp - maxOutOfOrderness - 1))
  }
}

final class StockPriceTimestampAssigner extends TimestampAssigner[StockPrice] {
  override def extractTimestamp(t: StockPrice, l: Long): Long = 0
}
