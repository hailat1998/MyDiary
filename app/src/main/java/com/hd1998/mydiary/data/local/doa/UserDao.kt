package com.hd1998.mydiary.data.local.doa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hd1998.mydiary.domain.model.User


@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE id= :id" )
            suspend fun getUser(id: String):User

            @Update
            suspend fun updateUser(user: User)

            @Insert
            suspend fun insertUser(user: User)

            @Query("DELETE FROM User")
            suspend fun delete()
}