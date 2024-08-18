package com.hd1998.mydiary.domain.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Immutable
@Entity
data class User(
    @PrimaryKey val id: String ,
    val name: String,
    val email: String,
    val date: Date= Date()
)