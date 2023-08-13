package com.osamaaftab.busright.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.domain.model.Resource
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.domain.repository.RouteRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class GetRouteDetailUseCaseTest {

    @RelaxedMockK
    lateinit var routeRepository: RouteRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var sut: GetRouteDetailUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        val errorHandler = mockk<ApiErrorHandle>()
        sut = GetRouteDetailUseCase(routeRepository, errorHandler)
    }

    @Test
    fun `run THEN verify the routes flow object`(): Unit =
        runBlocking {
            val responseModel = mockk<Resource<RouteModel>>(relaxed = true)
            val routesFlow = flow { emit(responseModel) }
            every { runBlocking { routeRepository.getRouteDetail("routeId") } } returns (routesFlow)

            val routes = sut.run("routeId")

            assertEquals(routesFlow, routes)
        }
}