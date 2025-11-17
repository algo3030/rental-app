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
import com.example.rentalapp.ui.MessageHost
import com.example.rentalapp.ui.Success
import com.example.rentalapp.ui.designsystem.components.Scaffold
import com.example.rentalapp.ui.designsystem.components.snackbar.SnackbarDuration
import com.example.rentalapp.ui.designsystem.components.snackbar.SnackbarHost
import com.example.rentalapp.ui.designsystem.components.snackbar.SnackbarHostState
import com.example.rentalapp.ui.screen.auth.AuthViewModel
import com.example.rentalapp.ui.screen.auth.LoginScreen
import com.example.rentalapp.ui.screen.auth.SignUpScreen
import com.example.rentalapp.ui.screen.error.ErrorScreen
import com.example.rentalapp.ui.screen.home.HomeScreen
import com.example.rentalapp.ui.screen.home.logic.HomeScreenViewModel
import com.example.rentalapp.ui.screen.loading.LoadingScreen
import io.github.jan.supabase.auth.status.SessionStatus

@Composable
fun RentalApp(
    messageHost: MessageHost
) {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()
    val sessionStatus = authViewModel.sessionState.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        messageHost.msg.collect { msg ->

            val message =
                when (msg) {
                    is Error -> when (msg) {
                        is Error.String -> msg.msg
                        Error.Unknown -> "Something went wrong"
                    }

                    is Success -> when (msg) {
                        is Success.String -> msg.msg
                    }
                }


            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    // セッションによる自動遷移
    LaunchedEffect(sessionStatus) {
        when (sessionStatus) {
            SessionStatus.Initializing -> Unit
            is SessionStatus.RefreshFailure -> navController.navigate(ErrorScreen)
            is SessionStatus.Authenticated -> navController.navigate(HomeScreen)
            is SessionStatus.NotAuthenticated -> navController.navigate(LoginScreen)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = LoadingScreen
        ) {
            composable<LoadingScreen> {
                LoadingScreen()
            }

            composable<ErrorScreen> {
                ErrorScreen()
            }

            composable<LoginScreen> {
                LoginScreen(
                    login = authViewModel::signIn,
                    navigateToSignUp = {
                        navController.navigate(SignUpScreen)
                    }
                )
            }
            composable<SignUpScreen> {
                SignUpScreen(
                    signUp = authViewModel::signUp
                )
            }

            composable<HomeScreen> {
                val viewModel: HomeScreenViewModel = hiltViewModel()

                val state = viewModel.state.collectAsStateWithLifecycle().value
                HomeScreen(
                    state = state,
                    onRefresh = {
                        viewModel.refresh()
                    })
            }
        }
    }
}