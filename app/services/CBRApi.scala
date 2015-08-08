package services

import java.text.SimpleDateFormat
import java.util.Calendar

import play.api.Logger
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.Play.current

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext


object CBRApi {

  def getCurrency(url: String): Future[JsArray] = {

    val dateL = Calendar.getInstance().getTime
    val format = new SimpleDateFormat("dd/MM/yyyy")
    val date_req = format.format(dateL)
    Logger.logger.info("with date=" + date_req)
    WS.url("http://" + url).withQueryString("date_req" -> date_req).get().map({
      response => {
        val xmlRes = response.xml \\ "ValCurs" \ "Valute"
        Logger.logger.info("response=" + xmlRes.size)
        val results: Seq[JsObject] = xmlRes.map(node =>
          Json.obj(
            "NumCode" -> JsString((node \ "NumCode").text),
            "CharCode" -> JsString((node \ "CharCode").text),
            "Name" -> JsString((node \ "Name").text),
            "Nominal" -> JsNumber((node \ "Nominal").text.toInt),
            "Value" -> JsNumber((node \ "Value").text.replace(",", ".").toDouble)
          )
        )

        val rs = Json.arr(results)
        //Logger.debug(s"json ${rs.toString()}")
        rs
      }
    })
  }
}
