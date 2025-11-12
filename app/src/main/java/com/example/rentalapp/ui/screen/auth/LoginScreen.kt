package com.example.rentalapp.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.rentalapp.ui.designsystem.components.Text
import com.example.rentalapp.ui.designsystem.components.textfield.OutlinedTextField
import com.example.rentalapp.ui.designsystem.components.textfield.TextField

@Composable
fun LoginScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.background)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
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
            }
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
            keyboardActions = KeyboardActions.Default
        )

        Spacer(Modifier.height(20.dp))

        Button(
            variant = ButtonVariant.Primary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.8f)
        ){
            Text(
                text = "Sign In",
                style = AppTheme.typography.button
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(
            variant = ButtonVariant.Ghost,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(
                text = "Create Account",
                style = AppTheme.typography.button
            )
        }
    }
}