package com.example.rentalapp.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository(
    private val supabase: SupabaseClient
) {
    suspend fun signUp(
        email: String,
        password: String
    ){
        supabase.auth.signUpWith(Email){
            this.email = email
            this.password = password
        }
    }
}