package com.example.rentalapp.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val supabase: SupabaseClient
): ViewModel() {
    val sessionState = supabase.auth.sessionStatus

    fun signUp(
        email: String,
        password: String
    ){
        viewModelScope.launch {
            supabase.auth.signUpWith(Email){
                this.email = email
                this.password = password
            }
        }
    }

    fun signIn(
        email: String,
        password: String
    ){
        viewModelScope.launch {
            supabase.auth.signInWith(Email){
                this.email = email
                this.password = password
            }
        }
    }
}