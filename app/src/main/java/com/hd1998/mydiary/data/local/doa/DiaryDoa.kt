package com.hd1998.mydiary.data.local.doa

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hd1998.mydiary.domain.model.Diary
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM Diary WHERE id = :id")
    suspend fun getDiaryById(id: String): Diary?

    @Query("SELECT * FROM Diary WHERE title LIKE :query OR text LIKE :query")
     fun searchDiary(query: String): Flow<List<Diary>?>

    @Query("SELECT * FROM Diary")
    fun getAllDiary(): PagingSource<Int, Diary>

    @Query("SELECT * FROM Diary")
    suspend fun getDiariesSnapshot(): List<Diary>

    @Insert
    suspend fun insertDiary(dairy: Diary)

    @Update
    suspend fun updateDiary(dairy: Diary)

    @Delete
    suspend fun deleteDiary(dairy: Diary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<Diary>)

    @Query("DELETE FROM Diary")
    suspend fun clearAll()
}