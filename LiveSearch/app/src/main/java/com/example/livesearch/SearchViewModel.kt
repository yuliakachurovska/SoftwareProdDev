package com.example.livesearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SearchViewModel : ViewModel() {

    private val searchRepository = SearchRepository()

    private val searchQueryFlow = MutableStateFlow("")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    @OptIn(FlowPreview::class)
    val searchResults: StateFlow<List<String>> = searchQueryFlow
        .debounce(400L)
        .filter { query -> query.trim().length >= 3 }
        .map { query -> query.trim() }
        .distinctUntilChanged()
        .map { query ->
            _isLoading.value = true
            try {
                searchRepository.search(query)
            } finally {
                _isLoading.value = false
            }
        }
        .flowOn(Dispatchers.IO)
        .catch { e ->
            // 6. Перехоплюємо помилки, щоб застосунок не впав
            Log.e("SearchFlow", "Помилка під час пошуку: ${e.message}")
            _isLoading.value = false
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun onQueryChanged(newQuery: String) {
        searchQueryFlow.value = newQuery
    }
}