package com.example.rentalapp.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rentalapp.ui.designsystem.AppTheme
import com.example.rentalapp.ui.designsystem.components.Button
import com.example.rentalapp.ui.designsystem.components.ButtonVariant
import com.example.rentalapp.ui.designsystem.components.Icon
import com.example.rentalapp.ui.designsystem.components.IconButton
import com.example.rentalapp.ui.designsystem.components.Text
import com.example.rentalapp.ui.designsystem.components.textfield.OutlinedTextField
import com.example.rentalapp.ui.designsystem.components.textfield.TextField
import kotlinx.serialization.Serializable

@Serializable
object SignUp

@Composable
fun SignUpScreen(
    signUp: (email: String, password: String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.background)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign Up",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp),
            style = AppTheme.typography.h1
        )

        var email by remember { mutableStateOf("") }
        OutlinedTextField(
            label = {
                Text(
                    text = "Email",
                    style = AppTheme.typography.label1
                )
            },
            value = email,
            onValueChange = {
                email = it
            },
            maxLines = 1
        )

        Spacer(Modifier.height(10.dp))

        var password by remember { mutableStateOf("") }
        OutlinedTextField(
            label = {
                Text(
                    text = "Password",
                    style = AppTheme.typography.label1
                )
            },
            value = "●".repeat(password.length),
            onValueChange = {
                password = it
            },
            keyboardActions = KeyboardActions.Default,
            maxLines = 1
        )

        Spacer(Modifier.height(10.dp))

        var passwordConfirm by remember { mutableStateOf("") }
        OutlinedTextField(
            label = {
                Text(
                    text = "Password (Confirm)",
                    style = AppTheme.typography.label1
                )
            },
            value = "●".repeat(passwordConfirm.length),
            onValueChange = {
                passwordConfirm = it
            },
            keyboardActions = KeyboardActions.Default,
            maxLines = 1
        )

        Spacer(Modifier.height(20.dp))

        Button(
            variant = ButtonVariant.Primary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.8f),
            onClick = {
                signUp(email,password)
            }
        ){
            Text(
                text = "Sign Up",
                style = AppTheme.typography.button
            )
        }
    }
}