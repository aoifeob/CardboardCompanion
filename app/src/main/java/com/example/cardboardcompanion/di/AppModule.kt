package com.example.cardboardcompanion.di

import android.content.Context
import androidx.room.Room
import com.example.cardboardcompanion.CardboardCompanionApp
import com.example.cardboardcompanion.data.CollectionDatabase
import com.example.cardboardcompanion.data.repository.CardRepository
import com.example.cardboardcompanion.data.repository.CollectionRepository
import com.example.cardboardcompanion.data.repository.LocalCollectionRepository
import com.example.cardboardcompanion.data.repository.NetworkCardRepository
import com.example.cardboardcompanion.data.sevice.ScryfallApiService
import com.example.cardboardcompanion.data.source.NetworkCardDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideApp(@ApplicationContext context: Context): CardboardCompanionApp =
        context as CardboardCompanionApp

    @Provides
    @Singleton
    fun provideCardDatabase(app: CardboardCompanionApp): CollectionDatabase {
        return Room.databaseBuilder(
            app,
            CollectionDatabase::class.java,
            "CardDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCollectionRepository(db: CollectionDatabase): CollectionRepository {
        return LocalCollectionRepository(db.cardDao)
    }

    @Provides
    @Singleton
    fun provideCardRepository(dataSource: NetworkCardDataSource): CardRepository {
        return NetworkCardRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideNetworkCardSource(api: ScryfallApiService): NetworkCardDataSource {
        return NetworkCardDataSource(api)
    }

    @Provides
    @Singleton
    fun provideScryfallApiService(): ScryfallApiService {
        return Retrofit.Builder()
            .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://api.scryfall.com")
            .build()
            .create(ScryfallApiService::class.java)
    }

}

private val jsonConfig = Json {
    ignoreUnknownKeys = true
}