package example.akka

import akka.persistence.journal.{EventAdapter, EventSeq}
import example.data.DataPointAdded
import example.domain._

class ProtoBuffAdapter extends EventAdapter {

  override def manifest(event: Any): String = "protobuf"

  override def toJournal(event: Any): Any = {
    event match {
      case DataPointAdded(point) => ProtoDataPointAdded(point)
      case _ => throw new NotImplementedError(s"Can't serialize ${event.getClass.getName}")
    }
  }

  override def fromJournal(event: Any, manifest: String): EventSeq = {
    val deserialized = event match {
      case ProtoDataPointAdded(point) => DataPointAdded(point)
      case _ => throw new NotImplementedError(s"Can't deserialize ${event.getClass.getName}")
    }
    EventSeq.single(deserialized)
  }
}
