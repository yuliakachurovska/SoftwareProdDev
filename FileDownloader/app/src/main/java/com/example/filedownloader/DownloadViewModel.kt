package com.example.filedownloader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DownloadUiState(
    val progress: Int = 0,
    val statusText: String = "Очікування завантаження",
    val isDownloading: Boolean = false
)

class DownloadViewModel : ViewModel() {

    private val fileRepository = FileRepository()

    private val _uiState = MutableStateFlow(DownloadUiState())
    val uiState: StateFlow<DownloadUiState> = _uiState.asStateFlow()

    private var downloadJob: Job? = null

    fun startDownload() {
        if (downloadJob?.isActive == true) return

        downloadJob = viewModelScope.launch {
            fileRepository.downloadFile()
                .onCompletion { cause ->
                    when (cause) {
                        null -> {
                            _uiState.update {
                                it.copy(
                                    progress = 100,
                                    statusText = "Файл успішно скачано!",
                                    isDownloading = false
                                )
                            }
                        }
                        is CancellationException -> {
                            _uiState.update {
                                it.copy(
                                    statusText = "Завантаження скасовано",
                                    isDownloading = false
                                )
                            }
                        }
                    }
                }
                .collect { progress ->
                    _uiState.update {
                        it.copy(
                            progress = progress,
                            statusText = "Завантаження: $progress%",
                            isDownloading = true
                        )
                    }
                }
        }
    }

    fun cancelDownload() {
        downloadJob?.cancel()
    }
}