package com.hd1998.mydiary.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd1998.mydiary.domain.model.Dairy
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository): ViewModel() {

    var dairyState by mutableStateOf<Dairy?>(null)
        private set

    fun getDairy(id: String) {
        viewModelScope.launch {
            repository.getDairyById(id).collect { dairy ->
                dairyState = dairy
            }
        }
    }
}