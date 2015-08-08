package controllers

import javax.inject.Inject

import models.{UserChart, ChartRepo}
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future


class Application @Inject()(repo: ChartRepo) extends Controller {

  def index = Action {
    Ok(views.html.index("", List(
      "/partials/chartForm.html",
      "/partials/collectorForm.html",
      "/partials/currencyDataForm.html"
    )))//, List("/partials/chartForm.html", ""))
  }

  case class cForm(id: Option[Long], name: String, period: Long)

  private val chartForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> text.verifying(nonEmpty),
      "period" -> longNumber.verifying()
    )(cForm.apply)(cForm.unapply)
  )

  def createChart = Action.async {implicit request =>

    chartForm.bindFromRequest.fold(
      formWithErrors => Future{BadRequest("invalid parameters")},
      form => {
        val entity = repo.create(UserChart(None, form.name, form.period))

        for (result <- entity) yield {
          Logger.debug(s"created $result")

          Created.withHeaders(LOCATION -> s"/charts/${result}")
          NoContent

        }

      }
    )

  }

  def getAll = Action.async {

    repo.getAll.map({vect =>
      val resp = for (chart <- vect) yield UserChart(Option(chart._1), chart._2, chart._3)
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
