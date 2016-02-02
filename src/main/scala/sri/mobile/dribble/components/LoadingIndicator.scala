package sri.mobile.dribble.components

import sri.universal.all._
import sri.universal.components._
import sri.universal.styles.UniversalStyleSheet

import scala.scalajs.js.{UndefOr => U}

object LoadingIndicator {

  val Component = () => {
    View(style = UniversalStyleSheet.wholeContainer)(
      View(style = styles.loadingContainer)(
        Text()("Loading ...")
      )
    )
  }

  object styles extends UniversalStyleSheet {

    val loadingContainer = style(flexOne,
      alignItems.center,
      justifyContent.center)
  }

  def apply() = createStatelessFunctionElementNoProps(Component)
}
