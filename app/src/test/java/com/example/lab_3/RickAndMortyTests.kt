package com.example.lab_3

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.example.lab_3.data.remote.RickAndMortyApiService
import com.example.lab_3.ui.screens.RickAndMortyUiState
import com.example.lab_3.ui.screens.RickAndMortyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
class RickAndMortyViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: RickAndMortyApiService
    private lateinit var viewModel: RickAndMortyViewModel

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApiService::class.java)

        viewModel = RickAndMortyViewModel()
    }

    @Test
    fun receiveDataFromApi_ReturnsTrue() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""{"results":[{"id":1,"name":"Rick Sanchez","species":"Human","status":"Alive","image":"https://rickandmortyapi.com/api/character/avatar/1.jpeg"}]}""")
        mockWebServer.enqueue(mockResponse)

        viewModel.fetchCharacters()
        viewModel.characters.test {
            val result = awaitItem()
            assert(result[0].name == "Rick Sanchez")
        }
    }

    @After
    fun shutdown() {
        mockWebServer.shutdown()
    }

    @Test
    fun networkErrorHandling_ReturnsTrue() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        viewModel.fetchCharacters()
        viewModel.characters.test {
            val result = awaitItem()
            assert(result.isEmpty())
        }
    }

    @Test
    fun correctUiUpdating_ReturnsTrue() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""{"results":[{"id":1,"name":"Rick Sanchez","species":"Human","status":"Alive","image":"https://rickandmortyapi.com/api/character/avatar/1.jpeg"}]}""")
        mockWebServer.enqueue(mockResponse)

        val states = mutableListOf<RickAndMortyUiState>()
        launch {
            viewModel.rickAndMortyUiState.toList(states)
        }

        viewModel.fetchCharacters()
        assert(
            states[0] == RickAndMortyUiState.Loading &&
            states[1] == RickAndMortyUiState.Success
        )
    }

    @Test
    fun coroutineCancelWhenViewModelDestroys_ReturnsTrue() = runTest {
        viewModel.fetchCharacters()
        viewModel.clearForTest()

        assert(!viewModel.viewModelScope.isActive)
    }
}