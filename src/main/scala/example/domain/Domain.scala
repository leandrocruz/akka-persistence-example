package example.domain

import java.util.Date

import play.api.libs.json.Writes.dateWrites
import play.api.libs.json._

/* Domain Model */
case class DataPoint(location: String, date: Date, value: Int)

/* Commands/Messages */
case class AddData(point: DataPoint)
case class AddDataReply(added: Boolean)
case class GetData(location: Option[String])
case class GetDataReply(entries: Seq[DataPoint])


object Domain {
  implicit val customDateWrites: Writes[java.util.Date] = dateWrites("yyyy-MM-dd'T'HH:mm:ss")
  implicit val dataPointWriter = Json.writes[DataPoint]
  implicit val dataPointReader = Json.reads[DataPoint]
}
