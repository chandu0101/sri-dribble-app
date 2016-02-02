package sri.mobile.dribble.components

import sri.mobile.ReactNative
import sri.mobile.all._
import sri.mobile.dribble.model.DribbleShot
import sri.mobile.examples.router.AppRouter.ShotDetailsPage
import sri.universal.components._
import sri.universal.router
import sri.universal.router.UniversalRouterComponent
import sri.universal.styles.UniversalStyleSheet

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined


object ShotCell {

  @ScalaJSDefined
  class Component extends UniversalRouterComponent[DribbleShot, Unit] {
    def render() = {
      View()(
        TouchableHighlight(onPress = () => navigateTo(ShotDetailsPage,props,props.title))(
          View(style = styles.row)(
            Image(source = ImageSource(uri = getShotImage(props)),
              style = styles.cellImage,
              accessible = true)()
          )
        ),
        View(style = styles.cellBorder)()
      )
    }
  }

  object styles extends UniversalStyleSheet {

    val row = style(
      backgroundColor := "white")
    val cellImage = style(
      height := 300,
      width := windowDimensions.width,
      backgroundColor := "transparent",
      resizeMode := "cover"
    )
    val cellBorder = style(
      backgroundColor := "rgba(0, 0, 0, 0.2)",
      // Trick to get the thinest line the device can display
      height := 1 / ReactNative.PixelRatio.get(),
      marginLeft := 4
    )
  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  ctor.contextTypes = router.routerContextTypes

  def apply(shot: DribbleShot, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, shot, key = key, ref = ref)

}
