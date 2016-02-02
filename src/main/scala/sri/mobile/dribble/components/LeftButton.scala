package sri.mobile.dribble.components

import sri.core._
import sri.mobile.dribble.styles.NavBarCustomTheme
import sri.universal.all._
import sri.universal.components._
import sri.universal.router
import sri.universal.router.UniversalRouterComponent
import sri.universal.styles.UniversalStyleSheet

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U}


object LeftButton {

  @ScalaJSDefined
  class Component extends UniversalRouterComponent[Unit, Unit] {
    def render() = {
      println(s"loading left button previous route : ${previousRoute.isDefined}")
            val backImage = loadDynamic("./assets/images/left-arrow.png")
      View(style = NavBarCustomTheme.navBarLeftButton)(
        (previousRoute.isDefined) ?= TouchableOpacity(onPress = () => navigateBack())(
          Image(style = styles.img, sourceDynamic = backImage)()
        )
      )
    }
  }

  object styles extends UniversalStyleSheet {

    val img = style(height := 28,
      width := 28,
      tintColor := "blue")

  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])
  ctor.contextTypes = router.routerContextTypes

  def apply(key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElementNoProps(ctor, key = key, ref = ref)
}
