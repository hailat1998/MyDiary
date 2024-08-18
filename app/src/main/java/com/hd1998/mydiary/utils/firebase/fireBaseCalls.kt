package com.hd1998.mydiary.utils.firebase

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hd1998.mydiary.domain.model.Diary
import kotlinx.coroutines.tasks.await

suspend fun deleteFirebase(diary: Diary, firestore: FirebaseFirestore, id: String){

   val docRef = firestore.collection("users").document(id)

    docRef.update("diaries", FieldValue.arrayRemove(diary)).await()
}


suspend fun updateFirebase(oldDiary: Diary, newDiary: Diary ,firestore: FirebaseFirestore, id: String){
    Log.i("Firebase local", "Updating")
    val docRef = firestore.collection("users").document(id)


    docRef.update("diaries", FieldValue.arrayRemove(oldDiary)).await()
    docRef.update("diaries", FieldValue.arrayUnion(newDiary)).await()


}

suspend fun addFirebase(diary: Diary, firestore: FirebaseFirestore, id: String){
    Log.i("Firebase local", "Updating")
    val docRef = firestore.collection("users").document(id)
    docRef.update("diaries", FieldValue.arrayUnion(diary)).await()
}