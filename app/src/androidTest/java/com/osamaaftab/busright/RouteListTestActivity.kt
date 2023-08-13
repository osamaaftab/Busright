package com.osamaaftab.busright

import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.widget.SearchView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.osamaaftab.busright.MockTestRunner.Companion.MOCK_WEB_SERVER_PORT
import com.osamaaftab.busright.common.OkHttp3IdlingResource
import com.osamaaftab.busright.common.RecyclerViewItemCountAssertion
import com.osamaaftab.busright.common.SuccessDispatcher
import com.osamaaftab.busright.common.typeSearchViewText
import com.osamaaftab.busright.presentation.ui.activity.RouteListActivity
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class RouteListTestActivity : KoinTest {

    private var mockWebServer = MockWebServer()
    private val client: OkHttpClient by inject()
    private var okHttp3IdlingResource: OkHttp3IdlingResource? = null

    @Before
    fun setup() {
        mockWebServer.start(MOCK_WEB_SERVER_PORT)
        Log.i(TAG, "Server is running on port number:$MOCK_WEB_SERVER_PORT")
        okHttp3IdlingResource = OkHttp3IdlingResource.create(OK_HTTP, client)
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
        Log.i(TAG, "Server has been shut down")
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }

    @Test
    fun onFetchRouteListSuccess() {
        mockWebServer.dispatcher = SuccessDispatcher()
        launchActivity<RouteListActivity>()
        onView(withId(R.id.indeterminateBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.routeRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun onFetchRouteListFails() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().throttleBody(1024, 5, TimeUnit.SECONDS)
            }
        }
        launchActivity<RouteListActivity>()
        onView(withId(R.id.indeterminateBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.statusImg)).check(matches(isDisplayed()))
    }

    @Test
    fun onFetchRouteListSuccessAndSearch() {
        mockWebServer.dispatcher = SuccessDispatcher()
        launchActivity<RouteListActivity>()
        onView(withId(R.id.search)).perform(click())
        onView(isAssignableFrom(SearchView::class.java)).perform(typeSearchViewText("23"))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.routeRecyclerView)).check(RecyclerViewItemCountAssertion(1))
    }

    companion object {
        private const val OK_HTTP = "okhttp"
        private val TAG = RouteListActivity::class.java.simpleName
    }
}