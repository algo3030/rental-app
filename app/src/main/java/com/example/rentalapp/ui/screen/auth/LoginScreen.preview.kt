package com.example.rentalapp.ui.screen.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
private fun Default(){
    LoginScreen(
        login = {email, pass -> },
        navigateToSignUp = {}
    )
}