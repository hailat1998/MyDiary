package com.hd1998.mydiary.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: Repository, private val coroutineDispatcher: CoroutineDispatcher): ViewModel()  {

    private val _screenState = MutableStateFlow(HomeScreenState(emptyList() ,  true))
    val screenState: StateFlow<HomeScreenState> = _screenState

    val diaryPagingData = loadNext()

    fun refresh(){
        loadWithPaging()
    }

   fun loadNext(): Flow<PagingData<Diary>>{
       return loadWithPaging().cachedIn(viewModelScope)
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
     private fun loadWithPaging(): Flow<PagingData<Diary>> = repository.getPager()


}