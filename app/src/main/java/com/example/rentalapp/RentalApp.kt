package com.example.rentalapp

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rentalapp.ui.Error
import com.example.rentalapp.ui.ErrorHost
import com.example.rentalapp.ui.Info
import com.example.rentalapp.ui.Message
import com.example.rentalapp.ui.designsystem.components.Scaffold
import com.example.rentalapp.ui.designsystem.components.snackbar.SnackbarDuration
import com.example.rentalapp.ui.designsystem.components.snackbar.SnackbarHost
import com.example.rentalapp.ui.designsystem.components.snackbar.SnackbarHostState
import com.example.rentalapp.ui.screen.auth.AuthViewModel
import com.example.rentalapp.ui.screen.auth.Login
import com.example.rentalapp.ui.screen.auth.LoginScreen
import com.example.rentalapp.ui.screen.auth.SignUp
import com.example.rentalapp.ui.screen.auth.SignUpScreen
import com.example.rentalapp.ui.screen.home.Home
import com.example.rentalapp.ui.screen.home.HomeScreen
import com.example.rentalapp.ui.screen.home.logic.HomeScreenViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.filter

@Composable
fun RentalApp(
    errorHost: ErrorHost
){
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()
    val sessionStatus = authViewModel.sessionState.collectAsStateWithLifecycle().value
    val loggedIn = sessionStatus is SessionStatus.Authenticated

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        errorHost.error.filter { it is Error }.collect { error ->
            val message = when (val err = error as Error) {
                is Error.String -> err.msg
                Error.Unknown -> "Something went wrong"
            }
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost ={ SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = if(!loggedIn) Login else Home
        ){
            composable<Login> {
                LoginScreen(
                    login = authViewModel::signIn,
                    navigateToSignUp = {
                        navController.navigate(SignUp)
                    }
                )

                LaunchedEffect(loggedIn) {
                    if (loggedIn) {
                        navController.navigate(Home) {
                            popUpTo(Login) { inclusive = true }
                        }
                    }
                }
            }
            composable<SignUp> {
                SignUpScreen(
                    signUp = authViewModel::signUp
                )
            }

            composable<Home> {
                val viewModel: HomeScreenViewModel = hiltViewModel()

                val state = viewModel.state.collectAsStateWithLifecycle().value
                HomeScreen(state)
            }
        }
    }
}