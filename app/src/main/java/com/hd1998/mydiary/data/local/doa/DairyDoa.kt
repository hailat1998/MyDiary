package com.hd1998.mydiary.data.local.doa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hd1998.mydiary.domain.model.Dairy
import kotlinx.coroutines.flow.Flow

@Dao
interface DairyDao {
    @Query("SELECT * FROM Dairy WHERE id = :id")
    suspend fun getDairyById(id: String): Dairy?

    @Query("SELECT * FROM Dairy WHERE text LIKE :query")
     fun searchDairy(query: String): Flow<List<Dairy>?>

    @Query("SELECT * FROM Dairy")
    fun getAllDairy(): Flow<List<Dairy>>

    @Insert
    suspend fun insertDairy(dairy: Dairy)

    @Update
    suspend fun updateDairy(dairy: Dairy)

    @Delete
    suspend fun deleteDairy(dairy: Dairy)
}