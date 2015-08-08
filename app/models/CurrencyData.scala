package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class CurrencyData(id: Option[Long], currencyName: String, dateChanged: Long, value: Double)
case class RawCBRData(numCode: String, charCode: String, name: String, nominal: Int, value: Double)

object CurrencyData {

  implicit def reads = Json.reads[CurrencyData]
  implicit def writes = Json.writes[CurrencyData]

  implicit val rawCBRReads: Reads[RawCBRData] = (
      (JsPath \ "NumCode").read[String] and
      (JsPath \ "CharCode").read[String] and
      (JsPath \ "Name").read[String] and
      (JsPath \ "Nominal").read[Int] and
      (JsPath \ "Value").read[Double]
    )(RawCBRData.apply _)

  implicit val rawCBRWrites: Writes[RawCBRData] = (
      (JsPath \ "NumCode").write[String] and
      (JsPath \ "CharCode").write[String] and
      (JsPath \ "Name").write[String] and
      (JsPath \ "Nominal").write[Int] and
      (JsPath \ "Value").write[Double]
    )(unlift(RawCBRData.unapply))
}