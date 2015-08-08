package models

import javax.inject.{Singleton, Inject}

import play.api.Logger
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext


trait CollectorsTableComponent {self: HasDatabaseConfigProvider[JdbcProfile]=>
  import driver.api._

  class CollectorsTable(tag: Tag) extends Table[Collector](tag, "collectors") {

    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def url = column[String]("url")
    def period = column[Long]("period")

    def * = (id, url, period) <> ((Collector.apply _).tupled, Collector.unapply _)
  }
}

@Singleton()
class CollectorsRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends RepoLike with CollectorsTableComponent {

  import driver.api._

  val collectors = TableQuery[CollectorsTable]

  def getAll: Future[Vector[(Long, String, Long)]] = {
    db.run(sql"select c.id, c.url, c.period from collectors as c".as[(Long, String, Long)])
  }

  def create(en: Collector): Future[Int] = {
    db.run(collectors += en)
  }

  def getCount: Future[Int] = {
    val resp = db.run(collectors.length.result)

    resp map {res =>
      Logger.debug(s"count = ${res.toString}")
    }
    resp
  }
}
