package de.mobile.crm.backend.campaign

import akka.actor.{ActorSystem, PoisonPill}
import akka.testkit.{ImplicitSender, TestKit}
import example.data.DataManager
import example.domain._
import org.scalatest._

import scala.concurrent.duration._

class DataManagerTest extends TestKit(ActorSystem("DataManagerTest", TestHelper.akkaPersistenceConfig()))
  with ImplicitSender
  with FlatSpecLike
  with Matchers
  with TestHelper
  with BeforeAndAfterAll
{
  override def afterAll {
    TestKit.shutdownActorSystem(system, verifySystemShutdown = true)
  }

  it should "test if GetData returns empty when there is no data" in {
    val clock = newClock()
    val ref = system.actorOf(DataManager.props(clock))
    val point = newPoint(clock)

    within(1 seconds) {
      ref ! GetData(None); expectMsg(GetDataReply(List()))
      ref ! AddData(point); expectMsg(AddDataReply(true))
      ref ! GetData(None); expectMsg(GetDataReply(List(point)))
    }

    ref ! PoisonPill

    val ref2 = system.actorOf(DataManager.props(clock))
    within(1 seconds) {
      ref2 ! GetData(None); expectMsg(GetDataReply(List(point)))
    }
  }
}