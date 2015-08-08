import javax.inject.Inject

import models.{UserChart, ChartRepo}
import play.api.GlobalSettings
import play.api.{Application, Logger}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Global extends GlobalSettings {

   override def onStart(app: Application) = {
     Logger.debug("Start app")
     //TODO - how to inject something to global?
  }

}
