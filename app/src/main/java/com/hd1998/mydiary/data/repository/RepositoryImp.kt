package com.hd1998.mydiary.data.repository

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hd1998.mydiary.data.local.db.DiaryDatabase
import com.hd1998.mydiary.data.local.doa.DiaryDao
import com.hd1998.mydiary.data.local.doa.UserDao
import com.hd1998.mydiary.data.remote.MyDiaryRemote
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.domain.model.User
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class RepositoryImp(
    private val database: DiaryDatabase,
    private val firestore: FirebaseFirestore,
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val coroutineDispatcher: CoroutineDispatcher,) : Repository {


    @OptIn(ExperimentalPagingApi::class)
   override fun getPager(): Flow<PagingData<Diary>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MyDiaryRemote(firestore = firestore, database = database,
                context = context, firebaseAuth = firebaseAuth, dispatcher = coroutineDispatcher),
            pagingSourceFactory = {database.dairyDao().getAllDiary() }
        ).flow
    }

    override suspend fun addUser(user: User) {
        println("Adding user")
        database.userDao().insertUser(user)
    }

    override suspend fun updateUser(user: User) {
       database.userDao().updateUser(user)
    }

    override suspend fun getUser(id: String): User? {
       return database.userDao().getUser(id)
    }


    override fun getAllDiary(): Flow<List<Diary>> {
           return flow{

           }
   }

    override fun searchDiary(query: String): Flow<List<Diary>?> {
        return try {
            database.dairyDao().searchDiary("%$query%")

        } catch (e: Exception) {
            println(e)
            flowOf(null)
        }
    }

    override  suspend fun getDiaryById(id: String): Flow<Diary?> {
        return flow {
            emit(database.dairyDao().getDiaryById(id))
        }.catch { e ->
            println(e)
            emit(null) // Emit null in case of an exception
        }
    }

    override suspend fun updateDiary(diary: Diary) {
        try {
            database.dairyDao().updateDiary(diary)
        } catch (e: Exception) {
            println(e)
        }
    }

    override suspend fun deleteDiary(diary: Diary) {
        try {
            database.dairyDao().deleteDiary(diary)
        } catch (e: Exception) {
            println(e)
        }
    }

    override suspend fun insertDiary(diary: Diary) {
        try {

            database.dairyDao().insertDiary(diary)
            Log.i("FROM_REPO", "inserted")
        } catch (e: Exception) {
           println(e)
        }
    }
}