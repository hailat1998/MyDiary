package com.hd1998.mydiary.domain.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID


@Immutable
@Entity
data class Diary(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                 var title: String, var text: String,
                 var date: Date = Date(),
                 var password: String? = null )
