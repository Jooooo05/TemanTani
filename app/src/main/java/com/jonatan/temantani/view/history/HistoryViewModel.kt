package com.jonatan.temantani.view.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    val history: LiveData<List<HistoryEntity>> = mHistoryRepository.getAllHistory().asLiveData()

    fun getAllHistory() {
        viewModelScope.launch {
            mHistoryRepository.getAllHistory()
        }
    }
}