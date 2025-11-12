package com.example.rentalapp.connection

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rentalapp.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.CodeVerifierCache
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConnectionTest {
    val supabaseUrl = BuildConfig.SUPABASE_URL
    val supabaseKey = BuildConfig.SUPABASE_KEY

    val supabase = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey
    ){
        install(Postgrest)
        install(Auth){
            sessionManager = InMemorySessionManager()
            codeVerifierCache = InMemoryCodeVerifierCache()
        }
    }

    @Test
    fun `DBと接続`() = runTest{
        val result = supabase.from("pcs").select()
        println(result.data)
    }

    @Test
    fun `サインアップ`() = runTest {
        val res = supabase.auth.signUpWith(Email){
            this.email = "3030.algo@gmail.com"
            this.password = "chinpo"
        }

        println(res)
    }

    @Test
    fun `ログイン`() = runTest {
        val collection = launch {
            supabase.auth.sessionStatus.onEach {
                when(it){
                    is SessionStatus.Authenticated -> {
                        println("Authed!")
                        println(it.session)
                    }
                    SessionStatus.Initializing -> println("Initializing")
                    is SessionStatus.NotAuthenticated -> println("Not authed.")
                    is SessionStatus.RefreshFailure -> println("Refresh failed")
                }
            }.collect()
        }

        supabase.auth.signInWith(Email){
            this.email = "3030.algo@gmail.com"
            this.password = "chinpo"
        }

        delay(100)
        collection.cancel()
    }
}

class InMemorySessionManager : SessionManager {
    private var currentSession: UserSession? = null

    override suspend fun saveSession(session: UserSession) {
        currentSession = session
        println("Session saved: ${session.accessToken.take(10)}...")
    }

    override suspend fun loadSession(): UserSession? {
        println("Session loaded: ${currentSession?.accessToken?.take(10) ?: "null"}")
        return currentSession
    }

    override suspend fun deleteSession() {
        println("Session deleted")
        currentSession = null
    }
}

class InMemoryCodeVerifierCache : CodeVerifierCache {
    private var codeVerifier: String? = null

    override suspend fun saveCodeVerifier(codeVerifier: String) {
        this.codeVerifier = codeVerifier
        println("CodeVerifier saved: ${codeVerifier.take(10)}...")
    }

    override suspend fun loadCodeVerifier(): String? {
        println("CodeVerifier loaded: ${codeVerifier?.take(10) ?: "null"}")
        return codeVerifier
    }

    override suspend fun deleteCodeVerifier() {
        println("CodeVerifier deleted")
        codeVerifier = null
    }
}