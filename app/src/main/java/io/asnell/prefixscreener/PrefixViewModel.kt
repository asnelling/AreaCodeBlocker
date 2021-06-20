package io.asnell.prefixscreener

import androidx.lifecycle.*
import io.asnell.prefixscreener.db.Prefix
import kotlinx.coroutines.launch

class PrefixViewModel(private val repository: Repository) : ViewModel() {
    val allPrefixes: LiveData<List<Prefix>> = repository.allPrefixes.asLiveData()

    fun insert(prefix: Prefix) = viewModelScope.launch {
        repository.insert(prefix)
    }

    fun delete(prefix: Prefix) = viewModelScope.launch {
        debug(TAG, "deleting prefix: $prefix")
        repository.delete(prefix)
    }

    companion object {
        private const val TAG = "PrefixViewModel"
    }
}

class PrefixViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrefixViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrefixViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
