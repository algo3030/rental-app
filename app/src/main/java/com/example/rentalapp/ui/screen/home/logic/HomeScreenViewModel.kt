package com.example.rentalapp.ui.screen.home.logic

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel: ViewModel() {
    val _state = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState())
    val state = _state.asStateFlow()
}