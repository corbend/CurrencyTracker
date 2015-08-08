package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

import actors.CollectorActor

class ActorGuiceModule extends AbstractModule with AkkaGuiceSupport {

    def configure() = {
        bindActor[CollectorActor]("collectorActor")
    }
}