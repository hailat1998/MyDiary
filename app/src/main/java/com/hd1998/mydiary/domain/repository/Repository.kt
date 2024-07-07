package com.hd1998.mydiary.domain.repository

import com.hd1998.mydiary.domain.model.Dairy
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllDairy() : Flow<List<Dairy>>
    fun searchDairy(query: String): Flow<List<Dairy>?>
    suspend fun getDairyById(id: String): Dairy?
    suspend fun updateDairy(dairy: Dairy)
    suspend fun deleteDairy(dairy: Dairy)
    suspend fun insertDairy(dairy: Dairy)

}