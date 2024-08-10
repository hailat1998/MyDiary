package com.hd1998.mydiary.data.remote

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hd1998.mydiary.data.local.db.DiaryDatabase
import com.hd1998.mydiary.data.local.doa.DiaryDao
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.utils.isInternetAvailable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class MyDiaryRemote(private val firestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth,
                    private val database: DiaryDatabase, private val context: Context ,
  private val dispatcher: CoroutineDispatcher): RemoteMediator<Int, Diary>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Diary>): MediatorResult {
        val id = firebaseAuth.currentUser?.uid
        return if(isInternetAvailable(context) && id != null){
            try {
                val docRef = firestore.collection("users").document(id)

                val fieldValue = withContext(dispatcher) {
                    val document = docRef.get().await()
                    if (document != null && document.exists()) {
                        val diaries = document.get("diaries") as? MutableList<Map<String, Any>> ?: mutableListOf()
                        diaries.map { data ->
                            val timestamp = data["date"] as? Timestamp
                            val date = timestamp?.toDate()
                            Diary(
                                id = data["id"] as? String ?: "",
                                title = data["title"] as? String ?: "",
                                text = data["text"] as? String ?: "",
                                password = data["password"] as? String,
                                date = date!!
                            )
                        }
                    } else {
                        println("No such document!")
                        mutableListOf()
                    }
                }

                val dbData = mutableListOf<Diary>()
                database.dairyDao().getDiariesLocal().collect {
                    dbData.addAll(it)
                }

                val allData = (fieldValue + dbData).toMutableSet()

                withContext(dispatcher) {
                    for (diary in dbData) {
                        if (!fieldValue.contains(diary)) {
                            try {
                                firestore.collection("users").document(id).update("diaries", FieldValue.arrayUnion(diary)).await()
                            } catch (e: Exception) {
                                println("Error adding diary: ${e.message}")
                            }
                        }
                    }
                }

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.dairyDao().clearAll()
                    }
                    database.dairyDao().insertAll(allData.toList())
                }

                MediatorResult.Success(endOfPaginationReached = allData.isEmpty())
            } catch (e: Exception) {
                MediatorResult.Error(e)
            }
        }else{
            MediatorResult.Error(Throwable("Internet unavailable"))
        }
     }
                    }