package com.hd1998.mydiary.data.repository

import com.hd1998.mydiary.data.local.db.DairyDatabase
import com.hd1998.mydiary.data.local.doa.DairyDao
import com.hd1998.mydiary.domain.model.Dairy
import com.hd1998.mydiary.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class RepositoryImp(private val dairyDao: DairyDao) : Repository {
    override fun getAllDairy(): Flow<List<Dairy>> {
        return try {
            dairyDao.getAllDairy()
        } catch (e: Exception) {
            println(e)
            emptyFlow()
        }
    }

    override fun searchDairy(query: String): Flow<List<Dairy>?> {
        return try {
            dairyDao.searchDairy("%$query%")
        } catch (e: Exception) {
            println(e)
            flowOf(null)
        }
    }

    override suspend fun getDairyById(id: String): Dairy? {
        return try {
            dairyDao.getDairyById(id)
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    override suspend fun updateDairy(dairy: Dairy) {
        try {
            dairyDao.updateDairy(dairy)
        } catch (e: Exception) {
            println(e)
        }
    }

    override suspend fun deleteDairy(dairy: Dairy) {
        try {
            dairyDao.deleteDairy(dairy)
        } catch (e: Exception) {
            println(e)
        }
    }

    override suspend fun insertDairy(dairy: Dairy) {
        try {
            dairyDao.insertDairy(dairy)
        } catch (e: Exception) {
           println(e)
        }
    }
}