package example.controllers

import javax.inject._

import com.typesafe.config.ConfigRenderOptions
import play.api.Configuration
import play.api.http.MimeTypes
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.mvc._

import scala.io.Source


@Singleton
class InternalController @Inject() (val env: play.api.Environment, val conf: Configuration) extends Controller {

  def ping() = Action {
    Ok("pong")
  }

  def config() = Action {
    Ok(conf.underlying.root().render(ConfigRenderOptions.concise())).as(MimeTypes.JSON)
  }

  def getenv() = Action {

    import scala.collection.JavaConversions._

    val sb = new StringBuffer("= ENV =\n")
    for ((key, value) <- System.getenv) {
      sb.append(key).append(" = ").append(value).append("\n")
    }

    sb.append("\n= PROPERTIES=\n")
    for ((key, value) <- System.getProperties) {
      sb.append(key).append(" = ").append(value).append("\n")
    }

    sb.append("--\n")

    Ok(sb.toString)
  }
}
