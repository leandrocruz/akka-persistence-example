package de.mobile.crm.backend.campaign

import java.time.Clock
import java.util.Date

import com.typesafe.config.ConfigFactory
import example.domain.DataPoint
import org.scalamock.scalatest.MockFactory

import scala.util.Random

object TestHelper {

  def akkaPersistenceConfig() = ConfigFactory.parseString(
    """
      |akka.loglevel = "off"
      |akka.persistence {
      |   journal.plugin = "inmemory-journal"
      |   snapshot-store.plugin = "inmemory-snapshot-store"
      |}
      |akka.actor {
      | serializers {
      |   proto = "akka.remote.serialization.ProtobufSerializer"
      | }
      | serialization-bindings {
      |   "example.domain.ProtoDataPointAdded" = proto
      | }
      |}
      |
      |inmemory-journal {
      |
      |event-adapters {
      |    protobuf = "example.akka.ProtoBuffAdapter"
      |}
      |
      |event-adapter-bindings {
      |  "example.domain.ProtoDataPointAdded" = protobuf
      |  }
      |}
    """.stripMargin('|')
  )

}

trait TestHelper extends MockFactory {

  def newClock(): Clock = {
    val clock = mock[Clock]
    val now = new Date()
    (clock.instant _).expects.anyNumberOfTimes.returning(now.toInstant)
    (clock.millis _).expects.anyNumberOfTimes.returning(now.getTime)
    clock
  }

  def newPoint(clock: Clock) = DataPoint(
    location = Random.alphanumeric.take(4).mkString,
    date = new java.util.Date(clock.millis - 1000),
    value = Random.nextInt
  )

}
