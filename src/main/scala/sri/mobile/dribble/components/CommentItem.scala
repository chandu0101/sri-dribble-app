package sri.mobile.dribble.components

import sri.core._
import sri.mobile.ReactNative
import sri.mobile.dribble.model.{DribbleShot, DribbleComment}
import sri.mobile.examples.router.AppRouter.ShotPlayerPage
import sri.universal.components._
import sri.mobile.all._
import sri.universal.router
import sri.universal.router.UniversalRouterComponent
import sri.universal.styles.UniversalStyleSheet
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, undefined => undefined}

object CommentItem {


  @ScalaJSDefined
  class Component extends UniversalRouterComponent[DribbleShot, Unit] {
    def render() = {
      View()(
        TouchableHighlight(onPress = () => navigateTo(ShotPlayerPage, props.user, props.user.name))(
          View(style = styles.commentComponent)(
            Image(source = authorAvatar(props.user))(),
            View(style = styles.commentBody)(
              Text(style = styles.userName)(props.user.name),
              Text(style = styles.commentText)(HTMLView(value = props.body)())
            ),
            View(style = styles.cellBorder)()
          )
        )
      )
    }
  }

  case class Props()

  object styles extends UniversalStyleSheet {

    val commentComponent = style(padding := 10,
      flexOne,
      flexDirection.row,
      alignItems.flexStart)

    val userName = style(fontWeight._700)

    val commentBody = style(flexOne,
      flexDirection.column,
      justifyContent.center)

    val commentText = style(flexOne, flexDirection.row)

    val cellBorder = style(backgroundColor := "rgba(0, 0, 0, 0.2)",
      height := 1 / ReactNative.PixelRatio.get(),
      marginLeft := 4)

    val avatar = style(borderRadius := 20,
      width := 40,
      height := 40,
      marginRight := 10)

  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  ctor.contextTypes = router.routerContextTypes

  def apply(comment: DribbleShot, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, comment, key = key, ref = ref)


}
