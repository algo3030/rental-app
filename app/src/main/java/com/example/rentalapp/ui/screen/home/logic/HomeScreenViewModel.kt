package com.example.rentalapp.ui.screen.home.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentalapp.data.RentalRepository
import com.example.rentalapp.model.RentalRequest
import com.example.rentalapp.model.RentalRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val rentalRepository: RentalRepository,
    private val supabase: SupabaseClient
): ViewModel() {
    val _state = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState())
    val state = _state.asStateFlow()

    init {
        loadActiveRental()
    }

    fun refresh() {
        loadActiveRental()
    }

    private fun loadActiveRental() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val userId = supabase.auth.currentUserOrNull()?.id

            if (userId == null) {
                _state.update {
                    it.copy(
                        rentalStatus = RentalStatus.NoRental,
                        isLoading = false
                    )
                }
                return@launch
            }

            runCatching {
                rentalRepository.getActiveRental(userId)
            }.onSuccess { rental ->
                _state.update {
                    it.copy(
                        rentalStatus = rental?.let { r ->
                            RentalStatus.Active(r.toDisplay())
                        } ?: RentalStatus.NoRental,
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "エラーが発生しました"
                    )
                }
            }
        }
    }

    private fun RentalRequest.toDisplay(): ActiveRentalDisplay {
        return ActiveRentalDisplay(
            id = id,
            pcNumber = pc.pcNumber,
            modelName = pc.model.modelName,
            manufacturer = pc.model.manufacturer,
            imageUrl = pc.model.imagePath?.let { path ->
                supabase.storage.from("pc-images").publicUrl(path)
            },
            startTime = startTime,
            endTime = endTime,
            status = when (status) {
                RentalRequestStatus.PENDING -> DisplayStatus.PENDING
                RentalRequestStatus.CHECKED_OUT -> DisplayStatus.CHECKED_OUT
                RentalRequestStatus.OVERDUE -> DisplayStatus.OVERDUE
                else -> DisplayStatus.PENDING
            },
            requestedAt = requestedAt
        )
    }
}