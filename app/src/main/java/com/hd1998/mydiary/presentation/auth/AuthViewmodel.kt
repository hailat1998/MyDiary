package com.hd1998.mydiary.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hd1998.mydiary.domain.model.User
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

const val TWO_WEEK_MILLI = 1209600000L

class AuthViewmodel(private val repository: Repository,
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth): ViewModel()  {

    private val _shouldLogIn = MutableStateFlow<Boolean>(true)
    val shouldLogIn get() = _shouldLogIn.asStateFlow()


    init{
        checkLogIn()
    }

    private fun checkLogIn(){
        viewModelScope.launch(dispatcher) {
            val id = firebaseAuth.currentUser?.uid
            if(id != null){
                val user = repository.getUser(id)
                val logInDay = user?.date?.time
                if(logInDay != null && Date().time - logInDay <= TWO_WEEK_MILLI){
                    _shouldLogIn.value = false
                }
            }
        }
    }
    fun updateUser(){
        viewModelScope.launch(dispatcher) {
            val id = firebaseAuth.currentUser?.uid
            if(id != null){
                val newUser = repository.getUser(id)?.copy(date = Date())
                repository.updateUser(newUser!!)
            }
        }
    }

    fun addUser(user: User){
        viewModelScope.launch(dispatcher) {
             repository.addUser(user)
        }
    }
}