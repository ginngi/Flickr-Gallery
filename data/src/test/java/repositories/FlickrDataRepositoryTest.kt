package repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Test
import org.mockito.Mockito.*

class FlickrDataRepositoryTest {
  private val apiRepository: FlickrApiRepository = mock(FlickrApiRepository::class.java)

  private val dataRepository = FlickrDataRepository(apiRepository)

  @After
  fun tearDown() {
    verifyNoMoreInteractions(apiRepository)
  }

  @ExperimentalCoroutinesApi
  @Test
  fun search() = runBlockingTest {
    dataRepository.search("", 1)

    verify(apiRepository).search("", 1)
  }

  @ExperimentalCoroutinesApi
  @Test
  fun getImageFromUrl() = runBlockingTest {
    dataRepository.getImageFromUrl("")

    verify(apiRepository).getImageFromUrl("")
  }
}