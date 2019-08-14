package com.rizn.akka

import scala.concurrent.duration._

object CurveArea extends App {
  val start: Long = System.currentTimeMillis()

  val result = AreaCalculator.calculateArea(
    AreaCalculator.defaultCurve, AreaCalculator.x1, AreaCalculator.x2
  )(1, AreaCalculator.segments)

  val duration = (System.currentTimeMillis() - start).millis

  println(s"result: ${BigDecimal(result).setScale(8, BigDecimal.RoundingMode.HALF_UP).toDouble}")
  println("completed in %s".format(duration))
}
