package sri.mobile.dribble.components

import chandu0101.macros.tojs.JSMacro

import scala.scalajs.js
import sri.core._
import sri.universal.components._
import sri.mobile.all._
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, undefined => undefined}


case class HTMLView(key: U[String] = undefined,
                    style: U[js.Any] = undefined,
                    stylesheet: U[js.Any] = undefined,
                    value: U[String],
                    ref: U[HTMLViewM => _] = undefined) {

  def apply() = {
    val props = JSMacro[HTMLView](this)
    val hv = loadDynamic("react-native-htmlview")
    React.createElement(hv, props)
  }

}

@js.native
trait HTMLViewM extends js.Object
