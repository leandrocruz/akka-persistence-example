package example.controllers

import javax.inject._

import akka.pattern.{AskTimeoutException, ask}
import example.akka.TopLevelActors
import example.domain.Domain._
import example.domain._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, JsSuccess}
import play.api.libs.json.Json._
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.duration._

@Singleton
class DataController @Inject() (val actors: TopLevelActors)
  extends Controller
{
  private def onTimeout(ex: Throwable) = {
    Logger.warn("Timeout", ex)
    GatewayTimeout
  }

  private def askActor(obj: Any): Future[Any] =
    actors.getDataManager().ask(obj) (200 millis)

  def addData() = Action.async(parse.json) {
    _.body.validate[DataPoint] match {
      case err: JsError =>
        Future.successful(BadRequest(JsError.toJson(err).toString()))

      case success: JsSuccess[DataPoint] =>
        askActor(AddData(success.get)) map {
            case None => NotFound
            case AddDataReply(result) => Ok(toJson(result))
          } recover {
            case e: AskTimeoutException => onTimeout(e)
          }
    }
  }

  def getData(location: Option[String]) = Action.async {
    askActor(GetData(location)) map {
        case None => NotFound
        case GetDataReply(entries) => Ok(toJson(entries))
      } recover {
        case e: AskTimeoutException => onTimeout(e)
      }
  }
}
