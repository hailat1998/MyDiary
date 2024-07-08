package com.hd1998.mydiary.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd1998.mydiary.domain.model.Dairy
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: Repository): ViewModel()  {

    private val _screenState = MutableStateFlow<HomeScreenState>(HomeScreenState(emptyList() ,  true))
    val screenState: StateFlow<HomeScreenState> = _screenState

    init {
        fetchDairyData()
    }

     fun fetchDairyData() {
        viewModelScope.launch {
            try {
                val data = repository.getAllDairy().collect { dairies ->
                    _screenState.value = HomeScreenState(dairies , false)
                }
            } catch (e: Exception) {
                _screenState.value = HomeScreenState(error = e.message ?: "unknown error")
            }
        }
    }
}