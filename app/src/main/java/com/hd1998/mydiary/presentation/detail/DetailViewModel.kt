package com.hd1998.mydiary.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository): ViewModel() {

    var dairyState by mutableStateOf<Diary?>(null)
        private set
   var saving by mutableStateOf(false)
    var deleting by mutableStateOf(false )

    fun getDiary(id: String) {
        viewModelScope.launch {
            repository.getDiaryById(id).collect { diary ->
                dairyState = diary
            }
        }
    }

    fun saveDiary(diary: Diary){
        println(diary)
        saving = true
        viewModelScope.launch {
            repository.insertDiary(diary)
        }
        println("saved")

        saving = false
    }

    fun deleteDiary(diary: Diary){
        deleting = true
        viewModelScope.launch {
            repository.deleteDiary(diary)
        }
        deleting = false
    }
}