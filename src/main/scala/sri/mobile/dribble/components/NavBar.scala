package sri.mobile.dribble.components

import org.scalajs.dom
import sri.core._
import sri.mobile.dribble.styles.NavBarCustomTheme
import sri.universal.all._
import sri.universal.components.{Text, View}
import sri.universal.router
import sri.universal.router.UniversalRouterComponent

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U}

object NavBar {

  @ScalaJSDefined
  class Component extends UniversalRouterComponent[Props, Unit] {
    def render() = {
      val route = currentRoute
      dom.window.console.log(s"current route", route)
      dom.window.console.log(s"number of routes :", getRouterCtrl().navigator.getCurrentRoutes())
      View(style = NavBarCustomTheme.navBar)(
        LeftButton(),
        Text(style = NavBarCustomTheme.navBarTitleText)(route.title),
        View(style = NavBarCustomTheme.navBarRightButton)(
          props.rightButton
        )
      )
    }
  }

  case class Props(rightButton: ReactElement)

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])
  ctor.asInstanceOf[js.Dynamic].contextTypes = router.routerContextTypes

  def apply(rightButton: ReactElement = null, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, Props(rightButton), key = key, ref = ref)


}
