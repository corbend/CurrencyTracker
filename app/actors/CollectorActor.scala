package actors

import javax.inject.Inject

import akka.actor.{Props, Actor}
import models.{RawCBRData, CurrencyData, CurrencyDataRepo}
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.json.{JsValue, Json, JsObject}
import play.api.libs.ws.WS
import services.CBRApi
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

class CollectorActor @Inject()(repo: CurrencyDataRepo) extends Actor {

  import models.CurrencyData._

  def receive = {
    case CollectorStart(url, period) =>
      Logger.debug(s"get message==>url=$url,period=$period")
      val response = CBRApi.getCurrency(url)

      response map { currencyArray =>
        //Logger.debug(s"parse data from CBR $currencyArray")
        for (resp: JsValue <- currencyArray.value) {

          val rawCbr = resp.as[List[RawCBRData]]

          val dataToSave = for (ent: RawCBRData <- rawCbr) yield CurrencyData(None, ent.charCode, DateTime.now().getMillis, ent.value)

          repo.addAll(dataToSave) map {_ =>
            Logger.debug("prepare to schedule")
            context.system.scheduler.scheduleOnce(10.seconds, context.self, CollectorStart(url, period))
          } recover {
            case err: Throwable => Logger.error("error on save data=>", err)
          }
        }
      } recover {
        case r: Throwable => Logger.error("error on parse data", r)
      }
    case _ =>
      Logger.debug(s"unhandled message")
  }
}
