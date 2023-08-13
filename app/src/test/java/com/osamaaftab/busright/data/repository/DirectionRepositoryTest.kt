package com.osamaaftab.busright.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.data.source.remote.DirectionRemoteDataSource
import com.osamaaftab.busright.domain.model.DirectionModel
import com.osamaaftab.busright.domain.model.StopModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DirectionRepositoryTest {

    @MockK
    lateinit var directionRemoteDataSource: DirectionRemoteDataSource

    @RelaxedMockK
    lateinit var apiErrorHandle: ApiErrorHandle

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var sut: DirectionRepositoryImp

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        sut = DirectionRepositoryImp(directionRemoteDataSource, apiErrorHandle)
    }

    @Test
    fun `getDirection from remote data source THEN verify`(): Unit =
        runBlocking {
            val stopModel = mockk<List<StopModel>>(relaxed = true)
            val directionModel = DirectionModel(listOf())

            every { runBlocking { directionRemoteDataSource.getDirectionAsync(stopModel) } } returns (CompletableDeferred(
                directionModel
            ))

            val direction = sut.getDirections(stopModel)

            assertEquals(direction.data, directionModel)
        }
}