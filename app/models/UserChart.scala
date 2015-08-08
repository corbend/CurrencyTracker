package models

import play.api.libs.json._

case class UserChart(id: Option[Long], name: String, period: Long)

object UserChart {

  implicit def reads: Reads[UserChart] = Json.reads[UserChart]
  implicit def writes: Writes[UserChart] = Json.writes[UserChart]

}

