package com.hd1998.mydiary.data.repository

import android.util.Log
import com.hd1998.mydiary.data.local.doa.DiaryDao
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class RepositoryImp(private val dairyDao: DiaryDao) : Repository {
    override fun getAllDiary(): Flow<List<Diary>> {
        return try {
            dairyDao.getAllDiary()
        } catch (e: Exception) {
            println(e)
            emptyFlow()
        }
    }

    override fun searchDiary(query: String): Flow<List<Diary>?> {
        return try {
            dairyDao.searchDiary("%$query%")

        } catch (e: Exception) {
            println(e)
            flowOf(null)
        }
    }

    override  suspend fun getDiaryById(id: String): Flow<Diary?> {
        return flow {
            emit(dairyDao.getDiaryById(id))
        }.catch { e ->
            println(e)
            emit(null) // Emit null in case of an exception
        }
    }

    override suspend fun updateDiary(diary: Diary) {
        try {
            dairyDao.updateDiary(diary)
        } catch (e: Exception) {
            println(e)
        }
    }

    override suspend fun deleteDiary(diary: Diary) {
        try {
            dairyDao.deleteDiary(diary)
        } catch (e: Exception) {
            println(e)
        }
    }

    override suspend fun insertDiary(diary: Diary) {
        try {

            dairyDao.insertDiary(diary)
            Log.i("FROM_REPO", "inserted")
        } catch (e: Exception) {
           println(e)
        }
    }
}