package com.ginzo.flirckrgallery.main

import android.graphics.Bitmap
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import usecases.GetImageFromUrlUseCase
import usecases.SearchPhotosUseCase

class MainPresenter constructor(
  private val view: MainView,
  private val searchPhotosUseCase: SearchPhotosUseCase,
  private val getImageFromUrlUseCase: GetImageFromUrlUseCase
) : DefaultLifecycleObserver {

  private lateinit var mainScope: CoroutineScope

  private val handler = CoroutineExceptionHandler { _, _ ->
    view.render(MainViewState.Error)
  }

  override fun onCreate(owner: LifecycleOwner) {
    view.render(MainViewState.Loading)

    mainScope = CoroutineScope(Job() + Main)

    mainScope.launch(handler) {
      getFlickrImages()
    }
  }

  override fun onDestroy(owner: LifecycleOwner) {
    mainScope.coroutineContext.cancel()
  }

  fun getImageFromUrl(url: String): Bitmap {
    return runBlocking {
      getImageFromUrlUseCase.getImageFromUrl(url)
    }
  }

  fun search(search: String) {
    view.render(MainViewState.Loading)

    mainScope.launch(handler) {
      getFlickrImages(search)
    }
  }

  private suspend fun getFlickrImages(search: String = "") {
    if (search.isNotEmpty()) {
      val photoPage = searchPhotosUseCase.search(search)
      view.render(MainViewState.ShowingFlickrImages(photoPage.photos))
    } else {
      view.render(MainViewState.EmptySearch)
    }
  }
}