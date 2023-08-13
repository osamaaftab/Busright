package com.osamaaftab.busright.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.data.source.remote.RouteRemoteDataSource
import com.osamaaftab.busright.domain.model.DirectionModel
import com.osamaaftab.busright.domain.model.PolyLineModel
import com.osamaaftab.busright.domain.model.Resource
import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.domain.repository.DirectionRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RouteRepositoryTest {

    @MockK
    lateinit var routeRemoteDataSource: RouteRemoteDataSource

    @RelaxedMockK
    lateinit var directionRepository: DirectionRepository

    @RelaxedMockK
    lateinit var apiErrorHandle: ApiErrorHandle

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var sut: RouteRepositoryImp


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        sut = RouteRepositoryImp(routeRemoteDataSource, directionRepository, apiErrorHandle)
    }

    @Test
    fun `getRoutes from remote data source THEN verify`(): Unit =
        runBlocking {
            val responseModel = ResponseModel(listOf())

            every { runBlocking { routeRemoteDataSource.getRoutesAsync() } } returns (CompletableDeferred(
                responseModel
            ))

            sut.getRoutes().collect {
                assertEquals(it.data, responseModel)
            }
        }

    @Test
    fun `getRouteDetails from remote data source and getDirection THEN verify if it is mapped to route object`(): Unit =
        runBlocking {
            val routeModel = RouteModel(
                "",
                "",
                listOf(),
                PolyLineModel("")
            )

            every {
                runBlocking { routeRemoteDataSource.getRouteDetailAsync("") }
            } returns (CompletableDeferred(
                routeModel
            ))

            val directionModel = DirectionModel(listOf(routeModel))

            every {
                runBlocking { directionRepository.getDirections(routeModel.stops) }
            } returns (Resource.Success(directionModel))

            sut.getRouteDetail("").collectLatest {
                verify(exactly = 1) {
                    runBlocking { directionRepository.getDirections(it.data!!.stops) }
                }
            }
        }
}