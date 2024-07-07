package com.hd1998.mydiary.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID


@Entity
data class Dairy(@PrimaryKey val id: String = UUID.randomUUID().toString(), val title: String, val text: String, val date: Date = Date())
