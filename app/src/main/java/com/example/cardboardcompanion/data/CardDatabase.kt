package com.example.cardboardcompanion.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.card.CardDao

@Database(entities = [Card::class], version = 1, exportSchema = false)
abstract class CardDatabase : RoomDatabase() {
    abstract val cardDao: CardDao
}