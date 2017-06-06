package example.akka

import java.time.Clock
import javax.inject._

import akka.actor.{ActorRef, ActorSystem}
import example.data.DataManager

trait TopLevelActors {
  def getDataManager(): ActorRef
}

@Singleton
class TopLevelActorsImpl @Inject() (system: ActorSystem, clock: Clock)
  extends TopLevelActors
{
  val dataManager = system.actorOf(DataManager.props(clock), "DataManager")
  def getDataManager() = dataManager
}
