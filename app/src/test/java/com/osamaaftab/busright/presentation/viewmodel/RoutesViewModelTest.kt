package com.osamaaftab.busright.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.usecase.GetRoutesUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RoutesViewModelTest {

    @RelaxedMockK
    lateinit var getRoutesUseCase: GetRoutesUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var sut: RoutesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        sut = RoutesViewModel(getRoutesUseCase)
    }

    @Test
    fun `getRoutesOnSuccess WHEN response is provided THEN verify progress bar live data value is false AND route list live data is not null`(): Unit =
        runBlocking {
            val responseModel = mockk<ResponseModel>(relaxed = true)

            sut.getRoutesSuccess(responseModel)

            val state = sut.getShowProgressLiveData().value
            val routeListLiveData = sut.getRouteListLiveData().value
            assertEquals(false, state)
            assertNotNull(routeListLiveData)
        }

    @Test
    fun `getRoutesFails WHEN error is provided THEN verify progress bar live data value is false and error live data value is true`() {
        val errorHandler = ApiErrorHandle()
        val throwable = mockk<Throwable>()
        errorHandler.traceErrorException(throwable)

        sut.getRoutesFails(errorHandler.traceErrorException(throwable))

        val state = sut.getShowProgressLiveData().value
        val drawable = sut.getShowErrorLiveData().value
        assertEquals(true, drawable)
        assertEquals(false, state)
    }

    @Test
    fun `loadRouteList WHEN routes view model is initialised THEN verify progress bar live data value is true and invoke is called`() {
        val state = sut.getShowProgressLiveData().value
        assertEquals(true, state)
        verify(exactly = 1) { getRoutesUseCase.invoke(any(), null, any(), any()) }
    }

    @Test
    fun `onRouteClick THEN verify the route id`() {
        sut.onRouteClick("")
        val state = sut.getOnRouteOnClick().value
        assertEquals("", state)
    }
}