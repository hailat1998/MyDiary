package com.hd1998.mydiary.data.remote

import android.content.Context
import android.util.Log
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
import com.hd1998.mydiary.utils.remoteHasRun
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class MyDiaryRemote(private val firestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth,
                    private val database: DiaryDatabase, private val context: Context ,
  private val dispatcher: CoroutineDispatcher): RemoteMediator<Int, Diary>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Diary>): MediatorResult {
        val id = firebaseAuth.currentUser?.uid
        Log.i("Remote" , "Started")
            return if(isInternetAvailable(context) && id != null && !remoteHasRun){

                Log.i("Remote" , "Started")
            try {
                Log.i("Remote" , "StartedTry")
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

                for(i in fieldValue){
                    println(i)
                }

                val dbData = mutableListOf<Diary>()
                withContext(dispatcher) {
                    println("Started with DB")
                    dbData.addAll(database.dairyDao().getDiariesSnapshot())
                }

                    println("-------------------------------------------")
                    println(dbData)
                    val allData = fieldValue.union(dbData)
                    println("-------------------------------------------")
                    println(allData)


                withContext(dispatcher) {
                    for (diary in dbData) {
                        println("in for Loop")
                        if (!fieldValue.contains(diary)) {
                            try {
                                firestore.collection("users").document(id).update("diaries", FieldValue.arrayUnion(diary)).await()
                            } catch (e: Exception) {
                                println("Error adding diary: ${e.message}")
                            }
                        }
                    }
                }

                withContext(dispatcher) {
                database.withTransaction {
                   database.dairyDao().insertAll(allData.toList())
                }
                    }
                  Log.i("Remote" , "Success")
                remoteHasRun = true
                MediatorResult.Success(endOfPaginationReached = true)
            } catch (e: Exception) {
                Log.i("Remote", "REMOTE_ERROR")
                MediatorResult.Error(e)
            }
        }else{
            Log.i("Remote", "Internet unavailable")
            MediatorResult.Error(Throwable("Internet unavailable"))
        }
     }
  }