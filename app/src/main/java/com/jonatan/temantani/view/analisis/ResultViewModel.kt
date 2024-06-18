package com.jonatan.temantani.view.analisis

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonatan.temantani.view.history.HistoryEntity
import com.jonatan.temantani.view.history.HistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> = _isError

    fun insertHistory(historyEntity: HistoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            mHistoryRepository.insertHistory(historyEntity)
        }
    }

    fun deleteHistory(historyEntity: HistoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            mHistoryRepository.deleteHistory(historyEntity)
        }
    }
}
