package controllers

import javax.inject.{Named, Inject}

import actors.{CollectorActor, CollectorStart, CollectorsPool}
import akka.actor.{ActorRef, Props, ActorSystem}
import models.{Collector, CollectorsRepo}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.Future
import akka.pattern._
import play.api.libs.concurrent.Execution.Implicits.defaultContext


class Collectors @Inject()(
  repo: CollectorsRepo, actorSystem: ActorSystem,
  @Named("collectorActor") actor: ActorRef) extends Controller {

  case class cForm(id: Option[Long], url: String, period: Long)

  private val chartForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "url" -> text.verifying(nonEmpty),
      "period" -> longNumber.verifying()
    )(cForm.apply)(cForm.unapply)
  )

  def createCollector = Action.async {implicit request =>

    chartForm.bindFromRequest.fold(
      formWithErrors => Future{BadRequest("invalid parameters")},
      form => {
        val model = Collector(None, form.url, form.period)
        val entity = repo.create(model)

        for (result <- entity) yield {
          Logger.debug(s"created $result")
          Logger.debug(s"send to actor - $actor")
          actor ! CollectorStart(model.url, model.period)
          Created.withHeaders(LOCATION -> s"/collectors/${result}")
          NoContent

        }

      }
    )

  }

  def getAll = Action.async {

    repo.getAll.map({vect =>
      val resp = for (chart <- vect) yield Collector(Option(chart._1), chart._2, chart._3)
      Logger.debug("recs=" + vect)
      Ok(Json.toJson(resp.map(Json.toJson(_))))
    })
  }

  def getCount = Action.async {
    repo.getCount map {count =>
      Ok(count.toString).as("text/html")
    } recover {case e =>
      Logger.error("error", e)
      BadRequest("no request")
    }
  }
}
