package com.example.rentalapp

import com.example.rentalapp.data.RentalRepository
import com.example.rentalapp.ui.MessageHost
import com.example.rentalapp.ui.commonExceptionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient{
        val supabaseUrl = BuildConfig.SUPABASE_URL
        val supabaseKey = BuildConfig.SUPABASE_KEY

        return createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ){
            install(Postgrest)
            install(Auth)
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideRentalRepository(
        supabase: SupabaseClient
    ): RentalRepository = RentalRepository(supabase)

    @Provides
    @Singleton
    fun provideMessageHost(): MessageHost = MessageHost()

    @Provides
    @Singleton
    fun provideCommonExceptionHandler(
        messageHost: MessageHost
    ): CoroutineExceptionHandler = commonExceptionHandler(messageHost)
}