package com.example.rentalapp.ui.screen.rental.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentalapp.data.RentalRepository
import com.example.rentalapp.ui.Error
import com.example.rentalapp.ui.MessageHost
import com.example.rentalapp.ui.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class RentalScreenViewModel @Inject constructor(
    private val rentalRepository: RentalRepository,
    private val supabase: SupabaseClient,
    private val messageHost: MessageHost,
    private val exceptionHandler: CoroutineExceptionHandler
): ViewModel() {

    private val _state = MutableStateFlow(RentalScreenUiState())
    val state = _state.asStateFlow()

    init {
        loadAvailablePcs()
    }

    private fun loadAvailablePcs() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            runCatching {
                rentalRepository.getAvailablePcs()
            }.onSuccess { pcs ->
                val pcDisplays = pcs.map { pc ->
                    PcDisplay(
                        id = pc.id,
                        pcNumber = pc.pcNumber,
                        modelName = pc.model.modelName,
                        manufacturer = pc.model.manufacturer,
                        imageUrl = rentalRepository.getPcImageUrl(pc.model.imagePath)
                    )
                }

                _state.update {
                    it.copy(
                        availablePcs = pcDisplays,
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load PCs."
                    )
                }
                messageHost.emit(Error.String(e.message ?: "Failed to load PCs."))
            }
        }
    }

    fun onPcSelected(pcId: String) {
        _state.update {
            it.copy(selectedPcId = pcId)
        }
    }

    fun onStartTimeSelected(startTime: Instant) {
        _state.update {
            it.copy(startTime = startTime)
        }
    }

    fun onEndTimeSelected(endTime: Instant) {
        _state.update {
            it.copy(endTime = endTime)
        }
    }

    fun onSubmitRequest() {
        val currentState = _state.value
        val pcId = currentState.selectedPcId ?: return
        val startTime = currentState.startTime ?: return
        val endTime = currentState.endTime ?: return

        viewModelScope.launch(exceptionHandler) {
            _state.update { it.copy(isSubmitting = true) }

            val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch

            rentalRepository.createRentalRequest(
                userId = userId,
                pcId = pcId,
                startTime = startTime,
                endTime = endTime
            ).onSuccess {
                messageHost.emit(Success.String("Rental request sended."))
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        isRequestCompleted = true
                    )
                }
            }.onFailure { e ->
                _state.update { it.copy(isSubmitting = false) }

                // エラーメッセージを分かりやすく変換
                val errorMessage = when {
                    e.message?.contains("already has an active rental") == true ->
                        "Already has an active rental"
                    e.message?.contains("already reserved") == true ->
                        "Already reserved"
                    else -> e.message ?: "Already reserved"
                }

                messageHost.emit(Error.String(errorMessage))
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
