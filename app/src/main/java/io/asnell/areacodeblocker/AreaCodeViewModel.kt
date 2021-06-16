package io.asnell.areacodeblocker

import android.util.Log
import androidx.lifecycle.*
import io.asnell.areacodeblocker.db.AreaCode
import kotlinx.coroutines.launch

class AreaCodeViewModel(private val repository: Repository) : ViewModel() {
    val allAreaCodes: LiveData<List<AreaCode>> = repository.allAreaCodes.asLiveData()

    fun insert(areaCode: AreaCode) = viewModelScope.launch {
        repository.insert(areaCode)
    }

    fun delete(areaCode: AreaCode) = viewModelScope.launch {
        Log.d(TAG, "deleting area code: $areaCode")
        repository.delete(areaCode)
    }

    companion object {
        private const val TAG = "AreaCodeViewModel"
    }
}

class AreaCodeViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AreaCodeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AreaCodeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
