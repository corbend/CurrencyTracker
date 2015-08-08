package models

import javax.inject.{Singleton, Inject}
import play.api.Logger
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

trait TableComponent {self: HasDatabaseConfigProvider[JdbcProfile]=>
  import driver.api._

  class UserChartsTable(tag: Tag) extends Table[UserChart](tag, "charts") {

    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def period = column[Long]("period")

    def * = (id, name, period) <> ((UserChart.apply _).tupled, UserChart.unapply _)
  }
}


@Singleton()
class ChartRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends TableComponent
  with HasDatabaseConfigProvider[JdbcProfile] with RepoLike {

  import driver.api._

  val charts = TableQuery[UserChartsTable]

  def getAll: Future[Vector[(Long, String, Long)]] = {
    var offset = 0
    val ls = for (chart <- charts) yield chart
    db.run(sql"select c.id, c.name, c.period from charts as c".as[(Long, String, Long)])
  }

  def create(chart: UserChart): Future[Int] = {
      db.run(charts += chart)
  }

  def getCount: Future[Int] = {
    val resp = db.run(charts.length.result)

    resp map {res =>
      Logger.debug(s"count = ${res.toString}")
    }
    resp
  }
}
