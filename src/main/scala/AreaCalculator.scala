package com.rizn.akka

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

object AreaCalculator {
  val segments = Int.MaxValue
  val x1 = 3
  val x2 = 10
  val defaultCurve = (x: Double) => curve1(x: Double)

  def curve1(x: Double) = -1.0/10 * Math.pow(x, 2) + 2 * x + 2
  def curve2(x: Double) = Math.sin(x) + 0.3 * x
  def curve3(x: Double) = Math.pow(Math.atan(x + 2), 0.5) - Math.sin(Math.log(Math.pow(x, 2)))

  def calculateArea(func: Double => Double, x1: Double, x2: Double, n: Int = Int.MaxValue)
                   (start: Int, end: Int): Double = {

    val deltaX = (x2 - x1)/n

    @tailrec
    def sum(k: Int, acc: Double): Double = {
      if (k < start) acc
      else sum(k-1, acc + deltaX * func(x1 + k * deltaX))
    }

    sum(end, 0)
  }

  @tailrec
  def segments(i: Int, num: Int, segment: Int, segmentList: ListBuffer[(Int, Int)]): ListBuffer[(Int, Int)] = {
    if (num < segment) {
      segmentList += ((i * segment + 1, i * segment + num))
      segmentList
    } else {
      segmentList += ((i * segment + 1, i * segment + segment))
      segments(i+1, num - segment, segment, segmentList)
    }
  }
}
