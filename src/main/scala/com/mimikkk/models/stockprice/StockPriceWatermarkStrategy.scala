package com.mimikkk.models.stockprice


import org.apache.flink.api.common.eventtime.{TimestampAssignerSupplier, Watermark, WatermarkGeneratorSupplier, WatermarkOutput, TimestampAssigner => TimestampAssignerBase, WatermarkGenerator => WatermarkGeneratorBase, WatermarkStrategy => WatermarkStrategyBase}


final class StockPriceWatermarkStrategy extends WatermarkStrategyBase[StockPrice] {
  override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context) = new WatermarkGenerator()

  override def createTimestampAssigner(context: TimestampAssignerSupplier.Context) = new TimestampAssigner()

  final class WatermarkGenerator extends WatermarkGeneratorBase[StockPrice] {
    private val maxOutOfOrderSeconds = 24 * 60 * 60 * 1000 // 24 hours
    private var maxTimestamp: Long = _

    override def onEvent(item: StockPrice, long: Long, output: WatermarkOutput): Unit = {
      maxTimestamp = math.max(0, maxTimestamp)
    }

    override def onPeriodicEmit(watermarkOutput: WatermarkOutput): Unit = {
      watermarkOutput.emitWatermark(new Watermark(maxTimestamp - maxOutOfOrderSeconds - 1))
    }
  }

  final class TimestampAssigner extends TimestampAssignerBase[StockPrice] {
    override def extractTimestamp(model: StockPrice, long: Long): Long = 0
  }
}
