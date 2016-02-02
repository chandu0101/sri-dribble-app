package sri.mobile.dribble.components

import chandu0101.macros.tojs.JSMacro
import sri.core._
import sri.universal.components._
import sri.mobile.all._
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, undefined => undefined}


case class ParallaxView(key: U[String] = undefined,
                        style: U[js.Any] = undefined,
                        backgroundSource: U[ImageSource] = undefined,
                        windowHeight: U[Double] = undefined,
                        blur: U[String] = undefined,
                        header: U[ReactElement] = undefined,
                        ref: U[ParallaxViewM => _] = undefined) {

  def apply(children: ReactNode*) = {
    val props = JSMacro[ParallaxView](this)
    val pv = loadDynamic("react-native-parallax-view")
    React.createElement(pv, props, children: _*)
  }

}

@js.native
trait ParallaxViewM extends js.Object
