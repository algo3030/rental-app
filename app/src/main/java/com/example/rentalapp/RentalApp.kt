package com.example.rentalapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rentalapp.ui.screen.auth.AuthViewModel
import com.example.rentalapp.ui.screen.auth.Login
import com.example.rentalapp.ui.screen.auth.LoginScreen
import com.example.rentalapp.ui.screen.auth.SignUp
import com.example.rentalapp.ui.screen.auth.SignUpScreen
import com.example.rentalapp.ui.screen.home.Home
import com.example.rentalapp.ui.screen.home.HomeScreen
import com.example.rentalapp.ui.screen.home.logic.HomeScreenViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import timber.log.Timber
import kotlin.math.log

@Composable
fun RentalApp(){
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()
    val sessionStatus = authViewModel.sessionState.collectAsStateWithLifecycle().value
    val loggedIn = sessionStatus is SessionStatus.Authenticated

    NavHost(
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
                if(loggedIn) navController.navigate(Home)
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