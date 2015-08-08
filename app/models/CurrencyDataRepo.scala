package models

import javax.inject.{Singleton, Inject}

import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext


trait CurrencyDataTableComponent {self: HasDatabaseConfigProvider[JdbcProfile]=>
  import driver.api._

  class CurrencyDataTable(tag: Tag) extends Table[CurrencyData](tag, "currency_data") {

    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def currencyName = column[String]("currency_name")
    def dateChanged = column[Long]("date_changed")
    def value = column[Double]("value")

    def * = (id, currencyName, dateChanged, value) <> ((CurrencyData.apply _).tupled, CurrencyData.unapply _)
  }
}

@Singleton()
class CurrencyDataRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends RepoLike with CurrencyDataTableComponent {

  import driver.api._

  val list = TableQuery[CurrencyDataTable]

  def getAll: Future[Vector[(Long, String, Long, Double)]] = {
    db.run(sql"select c.id, c.currency_name, c.date_changed, c.value from currency_data as c".as[(Long, String, Long, Double)])
  }

  def create(en: CurrencyData): Future[Int] = {
    db.run(list += en)
  }

  def addAll(ents: Seq[CurrencyData]) = {
    //TODO - why i need use that cast
    db.run(list.++=(ents).asInstanceOf[slick.dbio.DBIOAction[Some[Int],slick.dbio.NoStream,Nothing]])
  }

  def getCount: Future[Int] = {
    val resp = db.run(list.length.result)

    resp map {res =>
      Logger.debug(s"count = ${res.toString}")
    }
    resp
  }
}