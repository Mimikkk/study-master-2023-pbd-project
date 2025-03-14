package com.mimikkk.procesors

object UpdateStrategy extends Enumeration {
  type Type = Value

  final val Historical = Value("historical")
  final val Realtime = Value("realtime")

  def from(strategy: String): Type = strategy toLowerCase() match {
    case "historical" => Historical
    case "realtime" => Realtime
    case _ => throw new IllegalArgumentException(s"Unknown processing strategy: $strategy\n Available strategies: ${values mkString ", "}")
  }
}
