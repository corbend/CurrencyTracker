package controllers

import javax.inject.Inject


import models.{CurrencyData, CurrencyDataRepo}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext


class CurrenciesData @Inject()(repo: CurrencyDataRepo) extends Controller {

  def getAll = Action.async {

    repo.getAll.map({vect =>
      val resp = for (chart <- vect) yield CurrencyData(Option(chart._1), chart._2, chart._3, chart._4)
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
