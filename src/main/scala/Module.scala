
import java.time.Clock

import com.google.inject.AbstractModule
import example.akka.{TopLevelActors, TopLevelActorsImpl}

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    bind(classOf[TopLevelActors]).to(classOf[TopLevelActorsImpl]).asEagerSingleton
  }
}
