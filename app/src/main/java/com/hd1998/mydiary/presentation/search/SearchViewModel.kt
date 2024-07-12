package com.hd1998.mydiary.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository, private val coroutineDispatcher: CoroutineDispatcher) : ViewModel() {

    var searchResult by mutableStateOf(listOf<Diary>())
    private set


    fun search(query: String){
        viewModelScope.launch(coroutineDispatcher) {
            repository.searchDiary(query).collect{
                searchResult = it ?: emptyList()
            }
        }
    }
}