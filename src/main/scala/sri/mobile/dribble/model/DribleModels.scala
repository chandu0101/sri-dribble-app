package sri.mobile.dribble.model

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined


@ScalaJSDefined
trait DribbleShot extends js.Object {

  val images: DribbleImage
  val comment : DribbleComment
  val user : DribblePlayer
  val body : String
  val comments_url : String
  val title : String
  val likes_count : Int
  val comments_count : Int
  val views_count : Int
  val description : String

}

@ScalaJSDefined
trait DribbleImage extends js.Object {
  val normal: js.UndefOr[String]

  val teaser: js.UndefOr[String]

}

@ScalaJSDefined
trait DribblePlayer extends js.Object {

  val avatar_url : String

  val name : String

  val shots_url : String

  val username : String

  val followers_count : Int

  val shots_count : Int

  val likes_count : Int
}

@ScalaJSDefined
trait DribbleComment extends js.Object {
  val user : DribblePlayer
  val body : String
}