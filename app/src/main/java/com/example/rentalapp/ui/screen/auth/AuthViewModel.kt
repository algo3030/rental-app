package com.example.rentalapp.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentalapp.ui.MessageHost
import com.example.rentalapp.ui.Message
import com.example.rentalapp.ui.Success
import com.example.rentalapp.ui.commonExceptionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val supabase: SupabaseClient,
    private val messageHost: MessageHost,
    private val exceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    val sessionState = supabase.auth.sessionStatus

    fun signUp(
        email: String,
        password: String
    ) {
        viewModelScope.launch(exceptionHandler) {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            messageHost.emit(Success.String("Signed up!"))
        }
    }

    fun signIn(
        email: String,
        password: String
    ) {
        viewModelScope.launch(exceptionHandler) {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }
    }
}