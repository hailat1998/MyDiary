package com.hd1998.mydiary.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: Repository, private val coroutineDispatcher: CoroutineDispatcher): ViewModel()  {

    private val _screenState = MutableStateFlow<HomeScreenState>(HomeScreenState(emptyList() ,  true))
    val screenState: StateFlow<HomeScreenState> = _screenState

    init {
        fetchDairyData()
    }

   private fun fetchDairyData() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
               repository.getAllDiary().collect { dairies ->
                    _screenState.value = HomeScreenState(dairies , false)
                }
            } catch (e: Exception) {
                _screenState.value = HomeScreenState(error = e.message ?: "unknown error")
            }
        }
    }
}