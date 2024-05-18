package com.example.cardboardcompanion.di

import android.content.Context
import androidx.room.Room
import com.example.cardboardcompanion.CardboardCompanionApp
import com.example.cardboardcompanion.data.CardDatabase
import com.example.cardboardcompanion.data.repository.CardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideApp(@ApplicationContext context: Context): CardboardCompanionApp =
        context as CardboardCompanionApp

    @Provides
    @Singleton
    fun provideCardDatabase(app: CardboardCompanionApp): CardDatabase {
        return Room.databaseBuilder(
            app,
            CardDatabase::class.java,
            "CardDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCardRepository(db: CardDatabase): CardRepository {
        return CardRepository(db.cardDao)
    }
}