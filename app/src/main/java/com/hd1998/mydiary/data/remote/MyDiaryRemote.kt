package com.hd1998.mydiary.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.firebase.firestore.FirebaseFirestore
import com.hd1998.mydiary.data.local.db.DiaryDatabase
import com.hd1998.mydiary.data.local.doa.DiaryDao
import com.hd1998.mydiary.domain.model.Diary
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPagingApi::class)
class MyDiaryRemote(private val firestore: FirebaseFirestore,
                    private val database: DiaryDatabase): RemoteMediator<Int, Diary>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Diary>): MediatorResult {
        return try {
            val query = firestore.collection("diary-hd")
                .orderBy("date")
                .limit(state.config.pageSize.toLong())

            val snapshot = query.get().await()

            val data = snapshot.documents.map { document ->
                document.toObject(Diary::class.java)!!.copy(id = document.id)
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.dairyDao().clearAll()
                }
                database.dairyDao().insertAll(data)
            }

            MediatorResult.Success(endOfPaginationReached = data.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
                    }