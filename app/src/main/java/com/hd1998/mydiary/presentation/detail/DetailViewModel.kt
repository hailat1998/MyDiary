package com.hd1998.mydiary.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository, private val coroutineDispatcher: CoroutineDispatcher): ViewModel() {


    private var _diaryState = MutableStateFlow<Diary?>(null)
    val dairyState get() = _diaryState.asStateFlow()

   var saving by mutableStateOf(false)
    var deleting by mutableStateOf(false )

    fun getDiary(id: String) {
        viewModelScope.launch(coroutineDispatcher) {
            repository.getDiaryById(id).collect { diary ->
                _diaryState.value = diary
            }
        }
    }

    fun saveDiary(diary: Diary){
        println(diary)
        saving = true
        viewModelScope.launch(coroutineDispatcher) {
            repository.insertDiary(diary)
        }
        println("saved")

        saving = false
    }

    fun updateDiary(diary: Diary){
        println(diary)
        saving = true
        viewModelScope.launch(coroutineDispatcher) {
            repository.updateDiary(diary)
        }
        println("saved")

        saving = false
    }

    fun deleteDiary(diary: Diary){
        deleting = true
        viewModelScope.launch(coroutineDispatcher) {
            repository.deleteDiary(diary)
        }
        deleting = false
    }
}