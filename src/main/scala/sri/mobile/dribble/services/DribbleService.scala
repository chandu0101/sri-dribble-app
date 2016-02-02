package sri.mobile.dribble.services

import org.scalajs.dom
import org.scalajs.dom.ext.{Ajax, AjaxException}
import sri.mobile.dribble.model.DribbleShot
import scala.async.Async.{async, await}
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.util.{Left, Right}
import scala.scalajs.js.Dynamic.{literal => json}


object DribbleService {

  val API_URL = "https://api.dribbble.com/v1/"

  val ACCESS_TOKEN = "7a22f910dcdff63bd3ebbe48d022f05e8268c67249709b5489d923f97bcf96ec"

  val headers = Map(
    "Authorization" -> s"Bearer $ACCESS_TOKEN"
  )

  def getShotsByType(tpe: String, pageNUmber: Int = 0): Future[Either[ServiceException, js.Array[DribbleShot]]] = async {
    val url = s"${API_URL}shots/?list=$tpe&per_page=10&page=$pageNUmber"
    val result = await(Ajax.get(url,headers = headers))
    val resultDynamic = JSON.parse(result.responseText)
    if (js.isUndefined(resultDynamic.errors))
      Right(resultDynamic.asInstanceOf[js.Array[DribbleShot]])
    else Left(ServiceException(json(apiError = resultDynamic)))
  }.recover(catchError)

  def getResources(url : String): Future[Either[ServiceException,js.Array[DribbleShot]]] = async {
    println(s"calling resources url : $url")
     val result = await(Ajax.get(url,headers = headers))
    val resultDynamic = JSON.parse(result.responseText)
      dom.window.console.log(s"resource url response ", resultDynamic)
    if (js.isUndefined(resultDynamic.errors))
      Right(resultDynamic.asInstanceOf[js.Array[DribbleShot]])
    else Left(ServiceException(json(apiError = resultDynamic)))
  }.recover(catchError)


  def catchError[T] : PartialFunction[Throwable, Either[ServiceException,T]] = {
    case (ex: Throwable) => {
      var error : js.Dynamic = null
      if (ex.isInstanceOf[AjaxException]) {
         error = JSON.parse(ex.asInstanceOf[AjaxException].xhr.responseText)
        dom.window.console.log(s"Error calling service $API_URL ",error)
      }
      else {
        error = json(appError = ex.getMessage)
        dom.window.console.log(s"Error in App ",error)
      }
      Left(ServiceException(error))
    }
  }
}
