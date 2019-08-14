package com.rizn.akka

import akka.actor._
import akka.routing.RoundRobinPool
import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer

object CurveAreaAkka extends App {
  val segments = AreaCalculator.segments
  val divisor = segments/1000
  val rangeList = AreaCalculator.segments(0, segments, divisor, new ListBuffer[(Int, Int)]).toList
  val calcFunction = AreaCalculator.calculateArea(
    AreaCalculator.defaultCurve, AreaCalculator.x1, AreaCalculator.x2, segments
  ) _

  calc(numWorkers = 8, calcFunction, rangeList): Unit

  sealed trait AreaMessage
  case object Calculate extends AreaMessage
  case class Work(calcFunction: (Int, Int) => Double, range: (Int, Int)) extends AreaMessage
  case class Result(result: Double) extends AreaMessage
  case class FinalResult(elements: Double, duration: Duration) extends AreaMessage

  def calc(numWorkers: Int, calcFunction: (Int, Int) => Double, rangeList: List[(Int, Int)]) {
    val system = ActorSystem("CurveAreaSystem")
    val resultHandler = system.actorOf(Props[ResultHandler], name = "resultHandler")
    val master = system.actorOf(Props(new Master(numWorkers, resultHandler, calcFunction, rangeList)), name = "master")
    master ! Calculate
  }

  class Master(numWorkers: Int, resultHandler: ActorRef, calcFunction: (Int, Int) => Double, rangeList: List[(Int, Int)])
    extends Actor {
    var numResults: Double = 0
    var iter: Int = 0
    val start: Long = System.currentTimeMillis()

    val workerRouter = context.actorOf(
      Props[Worker].withRouter(RoundRobinPool(numWorkers)), name = "workerRouter")

    def receive = {
      case Calculate =>
        for (i <- 1 to rangeList.length) workerRouter ! Work(calcFunction, rangeList(i-1))
      case Result(result) =>
        numResults += result
        iter += 1
        if (iter == rangeList.length) {
          val duration = (System.currentTimeMillis() - start).millis
          resultHandler ! FinalResult(numResults, duration)
          context.stop(self)
        }
    }
  }

  class Worker extends Actor {
    def receive = {
      case Work(calcFunction, range) =>
        sender ! Result(calcFunction(range._1, range._2))
    }
  }

  class ResultHandler extends Actor {
    def receive = {
      case FinalResult(result, duration) =>
        println(s"result: ${BigDecimal(result).setScale(8, BigDecimal.RoundingMode.HALF_UP).toDouble}")
        println("completed in %s".format(duration))
        context.system.terminate()
    }
  }
}
