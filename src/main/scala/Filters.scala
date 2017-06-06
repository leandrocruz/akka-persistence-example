import javax.inject._
import play.api._
import play.api.http.DefaultHttpFilters
import play.filters.cors.CORSFilter

@Singleton
class Filters @Inject() (env: Environment, cors: CORSFilter, logging: LoggingFilter)
  extends DefaultHttpFilters(logging, cors)
{}
