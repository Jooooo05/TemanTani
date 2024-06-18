package com.jonatan.temantani.view.history

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistory(history: HistoryEntity)

    @Delete
    fun deleteHistory(history: HistoryEntity)

    @Query("SELECT * FROM history")
    suspend fun getAllHistory(): List<HistoryEntity>
}