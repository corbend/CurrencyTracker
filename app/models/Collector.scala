package models

import play.api.libs.json.Json


case class Collector(id: Option[Long], url: String, period: Long)


object Collector {

  implicit def reads = Json.reads[Collector]
  implicit def writes = Json.writes[Collector]
}
