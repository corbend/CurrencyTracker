package actors

import akka.actor.{Props, Actor}
import akka.routing.{RoundRobinPool}
import models.Collector

case class CollectorStart(url: String, period: Long)

class CollectorsPool extends Actor {

  val POOL = context.actorOf(Props[CollectorActor].withRouter(RoundRobinPool(10)), name="collectorRouter")

  def receive = {
    case _ =>
  }
}
