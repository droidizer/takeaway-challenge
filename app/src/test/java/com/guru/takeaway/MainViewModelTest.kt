package com.guru.takeaway

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.guru.takeaway.domain.IRestaurantDataSource
import com.guru.takeaway.model.Restaurant
import com.guru.takeaway.model.SortingValues
import com.guru.takeaway.schedulers.TestSchedulerProvider
import com.guru.takeaway.ui.utils.ISchedulersProvider
import com.guru.takeaway.ui.utils.loadingstate.LoadingState
import com.guru.takeaway.ui.viewmodel.MainViewModel
import io.reactivex.Single
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MainViewModelTest {

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var observer: Observer<MutableList<Restaurant>>

    @Mock
    private lateinit var stateObserver: Observer<LoadingState>

    private val mockDataSource: IRestaurantDataSource by lazy {
        Mockito.mock(IRestaurantDataSource::class.java)
    }

    private val mockSchedulerProvider: ISchedulersProvider = TestSchedulerProvider()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(mockDataSource, mockSchedulerProvider)
        viewModel.loadingStateLiveData.observeForever(stateObserver)
        viewModel.itemsLiveData.observeForever(observer)
    }

    @Test
    fun testNullValue() {
        Mockito.`when`(mockDataSource.getRestaurantList()).thenReturn(null)

        assertNotNull(viewModel.loadingStateLiveData)
        assertTrue(viewModel.loadingStateLiveData.hasObservers())
        assertNotNull(viewModel.itemsLiveData)
        assertTrue(viewModel.itemsLiveData.hasObservers())
    }

    @Test
    fun testHasValue() {
        Mockito.`when`(mockDataSource.getRestaurantList()).thenReturn(Single.just(getAllItems()))
        viewModel.loadRestaurants()

        Mockito.verify(observer, times(1)).onChanged(LoadingState.SUCCESS_STATE.data)
        Mockito.verify(stateObserver, times(0)).onChanged(LoadingState.ERROR_STATE)
        assertTrue(LoadingState.SUCCESS_STATE.data!!.isNotEmpty())
        assertTrue(LoadingState.SUCCESS_STATE.data!!.size == 3)
        assertEquals(getAllItems(), LoadingState.SUCCESS_STATE.data)
    }

    @Test
    fun testDataError() {
        Mockito.`when`(mockDataSource.getRestaurantList()).thenReturn(Single.error(Throwable()))
        viewModel.loadRestaurants()

        Mockito.verify(stateObserver, times(1)).onChanged(LoadingState.ERROR_STATE)
        Mockito.verify(observer, times(0)).onChanged(LoadingState.SUCCESS_STATE.data)
    }

    @Test
    fun testSearchReturnsValue() {
        viewModel.allItems.addAll(getAllItems())
        viewModel.subscribeForSearchChanges()
        viewModel.publishSearchChanges("Roti")

        Mockito.verify(observer, times(1)).onChanged(LoadingState.SUCCESS_STATE.data)
        Mockito.verify(stateObserver, times(0)).onChanged(LoadingState.ERROR_STATE)
        assertTrue(LoadingState.SUCCESS_STATE.data!!.isNotEmpty())
        assertTrue(LoadingState.SUCCESS_STATE.data!!.size == 2)
    }

    @Test
    fun testSearchReturnsEmpty() {
        viewModel.allItems.addAll(getAllItems())
        viewModel.subscribeForSearchChanges()
        viewModel.publishSearchChanges("Indian")

        Mockito.verify(observer, times(1)).onChanged(LoadingState.SUCCESS_STATE.data)
        Mockito.verify(stateObserver, times(0)).onChanged(LoadingState.ERROR_STATE)
        assertTrue(LoadingState.SUCCESS_STATE.data!!.isNullOrEmpty())
    }

    @Test
    fun testSearchMatchLowercase() {
        viewModel.allItems.addAll(getAllItems())
        viewModel.subscribeForSearchChanges()
        viewModel.publishSearchChanges("roti")

        Mockito.verify(observer, times(1)).onChanged(LoadingState.SUCCESS_STATE.data)
        Mockito.verify(stateObserver, times(0)).onChanged(LoadingState.ERROR_STATE)
        assertTrue(LoadingState.SUCCESS_STATE.data!!.isNotEmpty())
        assertTrue(LoadingState.SUCCESS_STATE.data!!.size == 2)
    }

    @Test
    fun testSearchTrimsWhiteSpaces() {
        viewModel.allItems.addAll(getAllItems())
        viewModel.subscribeForSearchChanges()
        viewModel.publishSearchChanges(" masala ro ti gh ar ")

        Mockito.verify(observer, times(1)).onChanged(LoadingState.SUCCESS_STATE.data)
        Mockito.verify(stateObserver, times(0)).onChanged(LoadingState.ERROR_STATE)
        assertTrue(LoadingState.SUCCESS_STATE.data!!.isNotEmpty())
        assertTrue(LoadingState.SUCCESS_STATE.data!!.size == 1)
    }

    @Test
    fun testSearchesName() {
        viewModel.allItems.addAll(getAllItems())
        viewModel.subscribeForSearchChanges()
        viewModel.publishSearchChanges("Masala Roti Ghar")

        assertTrue(LoadingState.SUCCESS_STATE.data!!.isNotEmpty())
        assertTrue(LoadingState.SUCCESS_STATE.data!!.size == 1)
        assertFalse(LoadingState.SUCCESS_STATE.data!![0]!!.status == "Masala Roti Ghar")
        assertTrue(LoadingState.SUCCESS_STATE.data!![0]!!.name == "Masala Roti Ghar")
    }

    private fun getAllItems(): MutableList<Restaurant> = mutableListOf(
        Restaurant("Roti Ghar", SortingValues(15, 12.50, 40, 2308, 12, 114.0, 15.00, 5.00), "open"),
        Restaurant("Masala Roti Ghar", SortingValues(15, 12.50, 40, 2308, 12, 114.0, 15.00, 5.00), "open"),
        Restaurant("Masala House", SortingValues(20, 22.50, 45, 2308, 12, 114.0, 15.00, 5.00), "open")
    )
}