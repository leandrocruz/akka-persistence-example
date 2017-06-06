package example.data

import java.time.Clock

import akka.actor.{ActorLogging, Props}
import akka.persistence._
import example.domain.{AddData, DataPoint}

import scala.collection.mutable.ListBuffer


case class DataPointAdded(point: DataPoint)

object DataManager {
  def props(clock: Clock) = Props(classOf[DataManager], clock)
}

class DataManager (val clock: Clock)
  extends PersistentActor with ActorLogging
{
  override def persistenceId = "data-manager"

  val buffer = ListBuffer[DataPoint]()

  private def canAdd(point: DataPoint): Boolean =
    clock.instant().isAfter(point.date.toInstant)

  def whenDataPointAdded(evt: DataPointAdded): Unit = buffer += evt.point

  override def receiveRecover: Receive = {
    case evt: DataPointAdded => whenDataPointAdded(evt)
    case RecoveryCompleted => log.info(s"[${persistenceId}|receiveRecover()] Recovery Completed")
    case msg => log.warning(s"[${persistenceId}|receiveRecover()] Bad message on receiveRecover '${msg.getClass.getName}'")
  }

  override def receiveCommand: Receive = {

    case AddData(point) =>
      if(canAdd(point)) {
        persist(DataPointAdded(point)) { evt =>
          whenDataPointAdded(evt)
          sender ! "bang"
        }
      } else {
          sender ! "nope"
      }

    case msg =>
      log.warning(s"[${persistenceId}] Unknown message on receiveCommand '${msg}'")
  }
}