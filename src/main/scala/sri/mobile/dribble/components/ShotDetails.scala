package sri.mobile.dribble.components

import sri.core._
import sri.mobile.ReactNative
import sri.mobile.all._
import sri.mobile.dribble.model.{DribblePlayer, DribbleShot}
import sri.mobile.dribble.services.DribbleService
import sri.mobile.examples.router.AppRouter.ShotPlayerPage
import sri.universal.components._
import sri.universal.router
import sri.universal.router.UniversalRouterComponent
import sri.universal.styles.UniversalStyleSheet
import scala.async.Async._
import scala.async.Async.{async, await}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, undefined}


object ShotDetails {


  case class State(isModalOpen: Boolean = false, isLoading: Boolean = true, dataSource: ListViewDataSource[DribbleShot, String] = createListViewDataSource((row1: DribbleShot, row2: DribbleShot) => row1 != row2))

  @ScalaJSDefined
  class Component extends UniversalRouterComponent[DribbleShot, State] {
    initialState(State())

    override def componentDidMount(): Unit = async {
      val result = await(DribbleService.getResources(props.comments_url))
      println(s"comments result : $result")
      println(s"comments result2 : ${result.isRight}")
      if (result.isRight) setState(state.copy(dataSource = state.dataSource.cloneWithRows(result.right.get), isLoading = false))
    }

    def render() = {
      println(s"rendering details with state $state")
      val player = props.user
      ParallaxView(windowHeight = 300.0,
        backgroundSource = ImageSource(getShotImage(props)),
        header = TouchableOpacity(onPress = () => openModal())(
          View(style = styles.invisibleView)()
        )
      )(
          View()(
            TouchableHighlight(onPress = () => navigateTo(ShotPlayerPage, player, player.name),
              underlayColor = "#333",
              activeOpacity = 0.95)(
                View(style = styles.headerContent)(
                  Image(source = authorAvatar(player), style = styles.playerAvatar)(),
                  Text(style = styles.shotTitle)(props.title),
                  Text(style = styles.playerContent)("by",
                    Text(style = styles.player)(player.name)
                  )
                )
              ),
            View(style = styles.mainSection)(
              View(style = styles.shotDetailsRow)(
                View(style = styles.shotCounter)(
                  Image(sourceDynamic = loadDynamic("./assets/images/heart.png"),style = styles.image)(),
                  Text(style = styles.shotCounterText)(props.likes_count)
                ),
                View(style = styles.shotCounter)(
                  Image(sourceDynamic = loadDynamic("./assets/images/comments.png"),style = styles.image)(),
                  Text(style = styles.shotCounterText)(props.comments_count)
                ),
                View(style = styles.shotCounter)(
                  Image(sourceDynamic = loadDynamic("./assets/images/eye.png"),style = styles.image)(),
                  Text(style = styles.shotCounterText)(props.views_count)
                )
              ),
              View(style = styles.separator)(),
              Text()(
                HTMLView(value = props.description, stylesheet = htmlStyles)()
              )
            ),
            View()(
              if (state.isLoading) LoadingIndicator()
              else
                ListView(renderRow = renderRow _,
                  automaticallyAdjustContentInsets = false,
                  keyboardDismissMode = keyboardDismissMode.ON_DRAG,
                  keyboardShouldPersistTaps = true,
                  showsVerticalScrollIndicator = false,
                  dataSource = state.dataSource)()
            )
          ),
          Modal(visible = state.isModalOpen)(
            Image(source = ImageSource(uri = getShotImage(props)),
              style = styles.customModalImage,
              resizeMode = ImageResizeMode.CONTAIN)())
        )
    }

    def openModal() = setState(state.copy(isModalOpen = true))

    def closeModal() = setState(state.copy(isModalOpen = false))

    def renderRow(shot: DribbleShot, secId: String, rowId: String) = ShotCell(shot)

  }

  case class Props()

  val htmlStyles = json(a = json(fontWeight = "300", color = "#ea4c89"), p = json(marginBottom = 0, flexDirection = "row", marginTop = 0))

  object styles extends UniversalStyleSheet {

    val spinner = style(marginTop := 20, width := 50)

    val invisibleView = style(
      flex := 1,
      position := "absolute",
      top := 0,
      left := 0,
      bottom := 0,
      right := 0
    )
    val customModalImage = style(
      height := windowDimensions.height / 2
    )
    val headerContent = style(
      flex := 1,
      paddingBottom := 20,
      paddingTop := 40,
      alignItems := "center",
      width := windowDimensions.width,
      backgroundColor := "#fff"
    )
    val shotTitle = style(
      fontSize := 16,
      fontWeight := "400",
      color := "#ea4c89",
      lineHeight := 18
    )
    val playerContent = style(
      fontSize := 12
    )
    val player = style(
      fontWeight := "900",
      lineHeight := 18
    )
    val playerAvatar = style(
      borderRadius := 40,
      width := 80,
      height := 80,
      position := "absolute",
      bottom := 60,
      left := windowDimensions.width / 2 - 40,
      borderWidth := 2,
      borderColor := "#fff"
    )
    val rightPane = style(
      flex := 1,
      flexDirection := "column",
      alignItems := "center"
    )
    val shotDetailsRow = style(
      flex := 1,
      alignItems := "center",
      justifyContent := "center",
      backgroundColor := "white",
      flexDirection := "row"
    )
    val shotCounter = style(
      flex := 2,
      alignItems := "center",
      justifyContent := "space-between"
    )
    val shotCounterText = style(
      color := "#333"
    )
    val mainSection = style(
      flex := 1,
      alignItems := "stretch",
      padding := 10,
      backgroundColor := "white"
    )
    val separator = style(
      backgroundColor := "rgba(0, 0, 0, 0.1)",
      height := 1 / ReactNative.PixelRatio.get(),
      marginVertical := 10
    )
    val sectionSpacing = style(
      marginTop := 20
    )
    val heading = style(
      fontWeight := "700",
      fontSize := 16
    )

    val image = style(height := 24,
      width := 24,
      tintColor := "#333")
  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  ctor.contextTypes = router.routerContextTypes

  def apply(shot: DribbleShot, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, shot, key = key, ref = ref)

}
