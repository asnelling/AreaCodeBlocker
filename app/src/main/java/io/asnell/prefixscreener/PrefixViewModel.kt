package io.asnell.prefixscreener

import android.app.Application
import androidx.lifecycle.*
import io.asnell.prefixscreener.db.Prefix
import kotlinx.coroutines.launch

class PrefixViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository by lazy {
        (application as PrefixScreenerApplication).repository
    }

    val allPrefixes: LiveData<List<Prefix>> =
        repository.allPrefixes.asLiveData()

    fun insert(prefix: Prefix) = viewModelScope.launch {
        repository.insert(prefix)
    }

    fun delete(prefix: Prefix) = viewModelScope.launch {
        repository.delete(prefix)
    }
}
