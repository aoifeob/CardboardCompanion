package com.example.cardboardcompanion.model.card

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    @ColumnInfo(name = "set_code")
    var set: String,
    @ColumnInfo(name = "collector_no")
    var collectorNo: String,
    var image: String,
    var price: Double,
    var quantity: Int
) {

    fun getDisplayPrice(): String {
        return "€%.${2}f".format(price)
    }

    fun getDisplayName(): String {
        return "(${set.uppercase(Locale.ENGLISH)}) $name"
    }

    fun isSameCard(other: Card): Boolean {
        return set == other.set &&
                collectorNo == other.collectorNo
    }

}

@Dao
interface CardDao {

    @Query("SELECT * FROM cards WHERE quantity > 0 ORDER BY name ASC")
    fun getOwnedCards(): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE quantity > 0 ORDER BY price DESC LIMIT 5")
    fun getTopOwnedCardsByPrice(): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE set_code LIKE :set AND collector_no LIKE :collectorNo")
    fun findCard(set: String, collectorNo: String): Flow<Card?>

    @Insert
    fun addCard(card: Card): Long?

    @Update
    fun updateCard(card: Card): Int?

    @Delete
    fun deleteCard(card: Card): Int?

}

@Serializable
data class ScryfallCard(
    var name: String,
    var set: String,
    @SerialName("collector_number")
    var collectorNo: String,
    @SerialName("prices")
    var price: ScryfallPrice,
    @SerialName("image_uris")
    var imageSource: ScryfallImageSource
) {

    fun mapToCard(): Card {
        return Card(
            name = name,
            set = set,
            collectorNo = collectorNo,
            price = price.eurPrice,
            image = imageSource.imageUri,
            quantity = 1
        )
    }

    fun getDisplayPrice(): String {
        return "€%.${2}f".format(price.eurPrice)
    }

    fun getDisplayDetails(): String {
        return "$name (${set.uppercase(Locale.ENGLISH)} $collectorNo)"
    }

}

@Serializable
data class ScryfallPrice(
    @SerialName("eur")
    val eurPrice: Double
)

@Serializable
data class ScryfallImageSource(
    @SerialName("normal")
    val imageUri: String
)