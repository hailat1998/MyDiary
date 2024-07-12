package com.hd1998.mydiary.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID


@Entity
data class Diary(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                     var title: String, val text: String,
                      var date: Date = Date(),
                     var password: String? = null )
