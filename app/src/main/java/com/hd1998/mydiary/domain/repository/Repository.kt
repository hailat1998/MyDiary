package com.hd1998.mydiary.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.domain.model.User
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllDiary() : Flow<List<Diary>>
    fun searchDiary(query: String): Flow<List<Diary>?>
    suspend fun getDiaryById(id: String): Flow<Diary?>
    suspend fun updateDiary(diary: Diary)
    suspend fun deleteDiary(diary: Diary)
    suspend fun insertDiary(diary: Diary)
    fun getPager(): Flow<PagingData<Diary>>
    suspend fun updateUser(user: User)
    suspend fun getUser(id: String): User?
    suspend fun addUser(user: User)
}