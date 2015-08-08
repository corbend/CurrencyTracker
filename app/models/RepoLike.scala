package models

import play.api.Logger
import play.api.db.slick.HasDatabaseConfigProvider
import play.libs.F.Tuple
import slick.driver.JdbcProfile
import slick.profile.SqlStreamingAction

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext


trait RepoLike extends HasDatabaseConfigProvider[JdbcProfile]
