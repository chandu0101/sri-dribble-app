package sri.mobile.examples

import sri.mobile.ReactNative
import sri.mobile.all._
import sri.mobile.examples.router.AppRouter
import scala.scalajs.js
import scala.scalajs.js.JSApp


object DribbleApp extends JSApp {

  def main() = {
    val root = createMobileRoot(
      AppRouter.router
    )
    ReactNative.AppRegistry.registerComponent("SriDribbleApp", () => root)
  }
}
