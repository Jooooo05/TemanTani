package com.jonatan.temantani.view.history

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepository(application: Application) {
    private val mHistoryDao: HistoryDao = HistoryDatabase.getInstance(application).historyDao()

    fun getAllHistory(): Flow<List<HistoryEntity>> {
        return flow {
            emit(mHistoryDao.getAllHistory())
        }
    }

    suspend fun insertHistory(historyEntity: HistoryEntity) {
        mHistoryDao.insertHistory(historyEntity)
    }

    suspend fun deleteHistory(historyEntity: HistoryEntity) {
        mHistoryDao.deleteHistory(historyEntity)
    }
}
