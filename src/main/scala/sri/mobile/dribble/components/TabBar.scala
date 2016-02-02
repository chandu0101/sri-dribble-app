package sri.mobile.dribble.components

import sri.mobile.all._
import sri.mobile.dribble.styles.Colors
import sri.mobile.examples.router.AppRouter._
import sri.universal.components._
import sri.universal.router
import sri.universal.router.{Page, StaticPage, UniversalRouterComponent}
import sri.universal.styles.UniversalStyleSheet

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSName, ScalaJSDefined}
import scala.scalajs.js.{UndefOr => U}

object TabBar {

  @ScalaJSDefined
  class Component extends UniversalRouterComponent[Unit, Unit] {
    def render() = {
      View(style = styles.container)(
        getMenuItem(AllPage, "All", loadDynamic("./assets/images/dribble.png")),
        getMenuItem(DebutsPage, "Debuts", loadDynamic("./assets/images/cup.png")),
        getMenuItem(AnimatedPage, "Animated", loadDynamic("./assets/images/heart.png")),
        getMenuItem(ReboundsPage, "Rebounds", loadDynamic("./assets/images/bulb.png"))
      )
    }


    @JSName("sShouldComponentUpdate")
    override def shouldComponentUpdate(nextProps: => Unit, nextState: => Unit): Boolean = {
      val page = currentRoute.page
      page.isInstanceOf[TabBarPage]
    }

    def getMenuItem(page: Page, title: String, image: js.Dynamic) = {
      val selected = page == currentRoute.page
      View(style = styles.block)(
        TouchableWithoutFeedback(onPress = () => gotoPage(page, selected))(
          View(style = styles.block)(
            Image(sourceDynamic = image, style = styles.image(selected))(),
            Text(style = styles.text(selected))(title)
          )
        )
      )
    }

    def gotoPage(page: Page, selected: Boolean) = {
      if (!selected) resetStackWithNewRoute(page.asInstanceOf[StaticPage])
    }

  }

  object styles extends UniversalStyleSheet {

    val container = style(flexDirection.row,
      height := 60,
      backgroundColor := Colors.whiteSmoke)

    val block =
      style(
        justifyContent.center,
        alignItems.center,
        flexOne)


    def text(selected: Boolean) = {
      val c = if (selected) Colors.pink else Colors.grey
      style(color := c, fontWeight._500)
    }

    def image(selected: Boolean) = {
      val c = if (selected) Colors.pink else Colors.grey
      style(tintColor := c,
        width := 28,
        height := 28)
    }
  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  ctor.contextTypes = router.routerContextTypes

  def apply(key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElementNoProps(ctor, key = key, ref = ref)
}
