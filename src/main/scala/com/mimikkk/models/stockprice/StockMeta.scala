package com.mimikkk.models.stockprice

import org.apache.flink.api.common.functions.RuntimeContext

import scala.io.Source

object StockMeta {
  def from(context: RuntimeContext): Map[String, String] = {
    val source = Source.fromFile(context.getDistributedCache.getFile(name))

    val nameByStockId = source
      .getLines()
      .drop(1)
      .map(_.trim)
      .map(_.split(",", 3))
      .filter(_.length == 3)
      .map(array => {
        val symbol = array(1)
        val name = array(2)
        symbol -> name
      })
      .toMap
    source.close()

    nameByStockId
  }

  final val name: String = "meta-file"
}
