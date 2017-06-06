package example.akka

import akka.persistence.journal.{EventAdapter, EventSeq}
import example.domain._

class ProtoBuffAdapter extends EventAdapter {

  override def manifest(event: Any): String = "protobuf"

  override def toJournal(event: Any): Any = {
    event match {
      case DataPointAdded(point) => ProtoDataPointAdded(toProto(point))
      case _ => throw new NotImplementedError(s"Can't serialize ${event.getClass.getName}")
    }
  }

  override def fromJournal(event: Any, manifest: String): EventSeq = {
    val deserialized = event match {
      case ProtoDataPointAdded(point) => DataPointAdded(fromProto(point.get))
      case _ => throw new NotImplementedError(s"Can't deserialize ${event.getClass.getName}")
    }
    EventSeq.single(deserialized)
  }

  def toProto(point: DataPoint): Option[ProtoDataPoint] =
    Some(
      ProtoDataPoint(
        location = point.location,
        date = point.date.getTime,
        value = point.value
      )
    )

  def fromProto(proto: ProtoDataPoint) =
    DataPoint(
      location = proto.location,
      date = new java.util.Date(proto.date),
      value = proto.value
    )
}

