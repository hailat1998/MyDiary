package com.hd1998.mydiary.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class User(
    @PrimaryKey val id: String ,
    val name: String,
    val email: String,
    val date: Date= Date()
)