package com.hd1998.mydiary.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd1998.mydiary.domain.model.Dairy
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    var searchResult by mutableStateOf(listOf<Dairy>())
    private set


    fun search(query: String){
        viewModelScope.launch {
            repository.searchDairy(query).collect{
                searchResult = it ?: emptyList()
            }
        }
    }
}