package sri.mobile.dribble.styles

import sri.mobile.all._
import sri.universal.components.DefaultNavigationBar

import scala.scalajs.js.{Dictionary, UndefOr => U}

object NavBarCustomTheme extends DefaultNavigationBar.Style {

  val h = if(isIOSPlatform) 64 else 54
  val pt = if(isIOSPlatform) 20 else 15

  override val navBar: Dictionary[Any] = styleE(super.navBar)(
    backgroundColor := Colors.whiteSmoke,
    paddingTop := pt,
    height := h
  )
  override val navBarTitleText: Dictionary[Any] = styleE(super.navBarTitleText)(color := "black",
    paddingTop := 6)

  override val navBarLeftButton: Dictionary[Any] = styleE(super.navBarLeftButton)(width := 100)

  override val navBarRightButton: Dictionary[Any] = styleE(super.navBarRightButton)(width := 100)

  override val navBarButtonText: Dictionary[Any] = super.navBarButtonText


}
