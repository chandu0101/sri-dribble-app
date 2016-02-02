package sri.mobile.dribble.components

import sri.core._
import sri.mobile.all._
import sri.mobile.dribble.model.{DribblePlayer, DribbleShot}
import sri.mobile.dribble.services.DribbleService
import sri.universal.components._
import sri.universal.styles.UniversalStyleSheet

import scala.async.Async.{async, await}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, undefined}


object Player {

  case class State(isModalOpen: Boolean = false, isLoading: Boolean = true, dataSource: ListViewDataSource[DribbleShot, String] = createListViewDataSource((row1: DribbleShot, row2: DribbleShot) => row1 != row2))

  @ScalaJSDefined
  class Component extends ReactComponent[DribblePlayer, State] {
    initialState(State())


    override def componentWillMount(): Unit = async {
      val result = await(DribbleService.getResources(props.shots_url))
      if (result.isRight) setState(state.copy(dataSource = state.dataSource.cloneWithRows(result.right.get), isLoading = false))
    }

    def render() = {
      ParallaxView(windowHeight = 260.0, backgroundSource = authorAvatar(props), blur = "dark",
        header = TouchableOpacity(onPress = () => openModal())(
          View(style = styles.headerContent)(
            View(style = styles.innerHeaderContent)(
              Image(source = authorAvatar(props), style = styles.playerAvatar)(),
              Text(style = styles.playerUsername)(props.username),
              Text(style = styles.playerName)(props.name),
              View(style = styles.playerDetailsRow)(
                View(style = styles.playerCounter)(
                  Image(sourceDynamic = loadDynamic("./assets/images/users.png"), style = styles.image)(),
                  Text(style = styles.playerCounterValue)(props.followers_count)
                ),
                View(style = styles.playerCounter)(
                  Image(sourceDynamic = loadDynamic("./assets/images/camera.png"), style = styles.image)(),
                  Text(style = styles.playerCounterValue)(props.shots_count)
                ),
                View(style = styles.playerCounter)(
                  Image(sourceDynamic = loadDynamic("./assets/images/heart.png"), style = styles.image)(),
                  Text(style = styles.playerCounterValue)(props.likes_count)
                )
              )
            )
          )
        )
      )(
          View()(
            if (state.isLoading) LoadingIndicator()
            else
              ListView(renderRow = renderRow _,
                automaticallyAdjustContentInsets = false,
                keyboardDismissMode = keyboardDismissMode.ON_DRAG,
                keyboardShouldPersistTaps = true,
                showsVerticalScrollIndicator = false,
                dataSource = state.dataSource)()
          ),
          Modal(visible = state.isModalOpen)(Image(source = authorAvatar(props), style = styles.playerImageModal)())
        )
    }

    def openModal() = setState(state.copy(isModalOpen = true))

    def closeModal() = setState(state.copy(isModalOpen = false))

    def renderRow(shot: DribbleShot, secId: String, rowId: String) = ShotCell(shot)

  }

  case class Props()


  object styles extends UniversalStyleSheet {

    val listStyle = style(
      flex := 1,
      backgroundColor := "red"
    )
    val listView = style(
      flex := 1,
      backgroundColor := "coral"
    )
    val spinner = style(
      width := 50
    )
    val headerContent = style(
      flex := 1,
      alignItems := "center",
      justifyContent := "center",
      backgroundColor := "transparent"
    )
    val innerHeaderContent = style(
      marginTop := 30,
      alignItems := "center"
    )
    val playerInfo = style(
      flex := 1,
      alignItems := "center",
      justifyContent := "center",
      backgroundColor := "white",
      flexDirection := "row"
    )
    val playerUsername = style(
      color := "#fff",
      fontWeight := "300"
    )
    val playerName = style(
      fontSize := 14,
      color := "#fff",
      fontWeight := "900",
      lineHeight := 18
    )
    //Player details list
    val playerDetailsRow = style(
      flex := 1,
      alignItems := "center",
      justifyContent := "center",
      flexDirection := "row",
      width := windowDimensions.width / 2,
      marginTop := 20
    )
    val playerCounter = style(
      flex := 1,
      alignItems := "center"
    )
    val playerCounterValue = style(
      color := "#fff",
      fontWeight := "900",
      fontSize := 14,
      marginTop := 5
    )
    val playerAvatar = style(
      width := 80,
      height := 80,
      borderRadius := 40,
      borderWidth := 2,
      borderColor := "#fff",
      marginBottom := 10
    )
    //Modal
    val playerImageModal = style(
      height := windowDimensions.height / 3,
      resizeMode := "contain"
    )
    //playerContent
    val playerContent = style(
      padding := 20)

    val image = style(height := 18,
      width := 18,
      tintColor := "#fff")
  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(player: DribblePlayer, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, player, key = key, ref = ref)


}
