package sri.mobile.dribble.components

import org.scalajs.dom
import org.scalajs.dom.ext.{AjaxException, Ajax}
import sri.core.{ReactElement, ReactComponent}
import sri.mobile.all._
import sri.mobile.components.ios.ActivityIndicatorIOS
import sri.mobile.dribble.model.DribbleShot
import sri.mobile.dribble.services.DribbleService
import scala.async.Async.{async,await}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


import sri.universal.ReactEvent
import sri.universal.components._
import sri.universal.styles.UniversalStyleSheet

import scala.async.Async._
import scala.scalajs.js
import scala.scalajs.js.{JSON, URIUtils, UndefOr}
import scala.scalajs.js.annotation.ScalaJSDefined

object ShotList {
  val API_URL = "http://api.rottentomatoes.com/api/public/v1.0/"
  val API_KEYS = List("7waqfqbprs7pajbz28mqf6vz", "y4vwv8m33hed9ety83jmv52f")


  val LOADING = collection.mutable.Map.empty[String, Boolean].withDefaultValue(false)

  case class ResultsCache(dataForQuery: Map[String, js.Array[DribbleShot]] = Map().withDefaultValue(js.Array()), nextPageNumberForQuery: Map[String, Int] = Map().withDefaultValue(0), totalForQuery: Map[String, Int] = Map().withDefaultValue(0))

  case class State(isLoading: Boolean = false, isLoadingTail: Boolean = false, filter: String = "", dataSource: ListViewDataSource[DribbleShot, String] = createListViewDataSource((row1: DribbleShot, row2: DribbleShot) => row1 != row2), queryNumber: Int = 0)


  @ScalaJSDefined
  class Component extends ReactComponent[String, State] {

    override def componentWillMount(): Unit = {
      initialState(State(filter = props))
    }

    def render() = {
      val content: ReactElement = if (state.dataSource.getRowCount() == 0) LoadingIndicator()
      else ListView[DribbleShot, String](
        dataSource = state.dataSource,
        renderRow = renderRow,
        onEndReached = onEndReached _,
        renderFooter = renderFooter _,
        showsVerticalScrollIndicator = false,
        keyboardShouldPersistTaps = true,
        automaticallyAdjustContentInsets = false
      )()
      View(style = styles.container)(
        View(style = styles.separator)(),
        content
      )

    }


    override def componentDidMount(): Unit = {
      getShots(state.filter)
    }

    var resultsCache = ResultsCache()

    var timeoutID: Int = _

    var listViewMounted: ListViewM = null

    def storeListViewRef(ref: ListViewM) = {
      if (!js.isUndefined(ref) && ref != null) listViewMounted = ref
    }


    def _urlForQueryAndPage(query: String, pageNumber: Int) = {
      val apiKey = API_KEYS(state.queryNumber % API_KEYS.length)
      if (query.nonEmpty) s"${API_URL}movies.json?apikey=${apiKey}&q=${URIUtils.encodeURIComponent(query)}&page_limit=20&page=$pageNumber"
      else s"${API_URL}lists/movies/in_theaters.json?apikey=${apiKey}&page_limit=20&page=${pageNumber}"
    }

    def getDataSource(movies: js.Array[DribbleShot]) = {
      state.dataSource.cloneWithRows(movies)
    }

    def getShots(query: String) = {
      val cachedResultsForQuery = resultsCache.dataForQuery.getOrElse(query, null)
      if (cachedResultsForQuery != null) {
        if (!LOADING.getOrElse(query, false)) {
          setState(state.copy(dataSource = getDataSource(cachedResultsForQuery), isLoading = false))
        } else {
          setState(state.copy(isLoading = true))
        }
      } else {
        LOADING += query -> true
        resultsCache = resultsCache.copy(dataForQuery = resultsCache.dataForQuery.updated(query, null))
        setState(state.copy(isLoading = true, queryNumber = state.queryNumber + 1, isLoadingTail = false))
        val page = resultsCache.nextPageNumberForQuery.getOrElse(query, 1)
        async {
          val result = await(DribbleService.getShotsByType(query, page))
          val shots : js.Array[DribbleShot] = if(result.isRight) result.right.get else js.Array()
          LOADING.update(query, false)
          resultsCache = resultsCache.copy(dataForQuery = resultsCache.dataForQuery.updated(query, shots),
            nextPageNumberForQuery = resultsCache.nextPageNumberForQuery.updated(query, 2))
           setState(state.copy(isLoading = false, dataSource = getDataSource(shots)))
        }.recover {
          case ex => {
            LOADING.update(query, false)
            setState(state.copy(isLoading = false))
            println(s"Error searching movies with query $query -> ${ex.asInstanceOf[AjaxException].xhr.responseText}")
          }
        }
      }

    }

    def hasMore = {
      val query = state.filter
      if (resultsCache.dataForQuery.getOrElse(query, null) == null) true
      else resultsCache.totalForQuery(query) != resultsCache.dataForQuery(query).length
    }

    def onEndReached: Unit = {
      val query = state.filter
      if (hasMore || !state.isLoadingTail || !LOADING(query)) {
        // if we have all elements or fetching don't do anything
        LOADING += query -> true
        setState(state.copy(queryNumber = state.queryNumber + 1, isLoadingTail = true))
        val page = resultsCache.nextPageNumberForQuery(query)
        async {
          val result = await(DribbleService.getShotsByType(query, page))
          val moviesForQuery = resultsCache.dataForQuery(query)
          LOADING.update(query, false)
          if (result.isLeft) {
            resultsCache = resultsCache.copy(totalForQuery = resultsCache.totalForQuery.updated(query, moviesForQuery.length))
          } else {
            val shots = result.right.get
            shots.foreach(m => moviesForQuery.push(m))
            resultsCache = resultsCache.copy(dataForQuery = resultsCache.dataForQuery.updated(query, moviesForQuery),
              nextPageNumberForQuery = resultsCache.nextPageNumberForQuery.updated(query, resultsCache.nextPageNumberForQuery(query) + 1))
          }
          setState(state.copy(isLoadingTail = false, dataSource = getDataSource(resultsCache.dataForQuery(query))))
        }
      }
    }


    def renderRow(shot: DribbleShot, sectionID: String, rowID: String): ReactElement = {
      ShotCell(shot = shot, key = shot.title.toString)
    }

    def renderFooter = {
      if (!hasMore || !state.isLoadingTail) View(style = styles.scrollSpinner)()
      else Spinner()
    }

    def onSearchInputFocus(e: ReactEvent) = {
      if (listViewMounted != null) listViewMounted.getScrollResponder().scrollTo(0, 0)
    }
  }


  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(filter: String, key: UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, filter, key = key, ref = ref)


  object styles extends UniversalStyleSheet {

    val container = style(
      flex := 1,
      backgroundColor := "white"
    )

    val separator = style(
      height := 1,
      backgroundColor := "#eeeeee"
    )

    val scrollSpinner = style(
      marginVertical := 20
    )

  }

}
