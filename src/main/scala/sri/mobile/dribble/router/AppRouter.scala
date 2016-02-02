package sri.mobile.examples.router

import sri.core.ReactElement
import sri.mobile.dribble.components._
import sri.mobile.dribble.model.{DribblePlayer, DribbleShot}
import sri.universal.components.View
import sri.universal.router._
import sri.universal.styles.UniversalStyleSheet


object AppRouter {

  sealed trait TabBarPage extends StaticPage

  object AllPage extends TabBarPage

  object DebutsPage extends TabBarPage

  object AnimatedPage extends TabBarPage

  object ReboundsPage extends TabBarPage

  object ShotDetailsPage extends DynamicPage[DribbleShot]

  object ShotPlayerPage extends DynamicPage[DribblePlayer]

  object SecondPage extends TabBarPage

  object Config extends UniversalRouterConfig {

    override val initialRoute = defineInitialRoute(AllPage, "All", TabBarWrapper(ShotList("default")))

    staticRoute(DebutsPage, "Debuts", TabBarWrapper(ShotList("debuts")))

    staticRoute(AnimatedPage, "Animated", TabBarWrapper(ShotList("animated")))

    staticRoute(ReboundsPage, "Rebounds", TabBarWrapper(ShotList("rebounds")))

    dynamicRoute(ShotDetailsPage, (shot: DribbleShot) => TabBarWrapper(ShotDetails(shot)))

    dynamicRoute(ShotPlayerPage, (player: DribblePlayer) => TabBarWrapper(Player(player)))

    override val notFound: (StaticPage, NavigatorRoute) = initialRoute

    override def renderScene(route: NavigatorRoute): ReactElement = {
      View(style = UniversalStyleSheet.wholeContainer)(
        NavBar(),
        super.renderScene(route)
      )
    }
  }

  val router = UniversalRouter(Config)

}

