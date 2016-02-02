package sri.mobile.dribble.components

import sri.core._
import sri.universal.components._
import sri.mobile.all._
import sri.universal.styles.UniversalStyleSheet
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, undefined => undefined}


object TabBarWrapper {


  val Component = (children: ReactElement) => View(style = UniversalStyleSheet.wholeContainer)(
    children,
    TabBar()
  )

  object styles extends UniversalStyleSheet {


  }

  def apply(children: ReactElement) = createStatelessFunctionElementNoPropsWithChildren(Component)(children)
}
