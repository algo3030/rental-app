package com.example.rentalapp.data

import com.example.rentalapp.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    @Provides
    fun provideSupabaseClient(): SupabaseClient{
        val supabaseUrl = BuildConfig.SUPABASE_URL
        val supabaseKey = BuildConfig.SUPABASE_KEY

        return createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ){
            install(Postgrest)
            install(Auth)
        }
    }
}