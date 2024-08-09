package com.hd1998.mydiary.utils.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.hd1998.mydiary.domain.model.Diary
import kotlinx.coroutines.tasks.await

suspend fun deleteFirebase(diary: Diary, firestore: FirebaseFirestore){
    firestore.collection("diary-hd").document(diary.id)
        .delete().await()
}


suspend fun updateFirebase(diary: Diary, firestore: FirebaseFirestore){
    firestore.collection("diary-hd").document(diary.id).set(diary).await()
}