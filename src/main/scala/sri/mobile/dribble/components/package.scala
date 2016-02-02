package sri.mobile.dribble

import sri.core.all._
import sri.mobile.dribble.model.{DribblePlayer, DribbleShot}
import sri.universal.components.ImageSource

import scala.scalajs.js

package object components {

  def getShotImage(shot: DribbleShot) = shot.images.normal.getOrElse(shot.images.teaser.getOrElse(""))

  def authorAvatar(player : DribblePlayer) = {
    if(player != null && !js.isUndefined(player)) ImageSource(player.avatar_url)
    else {
      val limage = loadDynamic("./assets/images/AuthorAvatar.png")
      ImageSource(limage.uri.toString)
    }
  }

}
