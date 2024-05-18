package com.example.cardboardcompanion.model.card

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import com.example.cardboardcompanion.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    @SerialName("set_code")
    var set: String,
    @SerialName("collector_no")
    var collectorNo: Int,
    var image: Int,
    var price: Double,
    var quantity: Int
) {

    fun getDisplayPrice(): String {
        return "€%.${2}f".format(price)
    }

    fun getDisplayName(): String {
        return "($set) $name"
    }

    fun getDisplayDetails(): String {
        return "$name ($set $collectorNo)"
    }

}

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY name ASC")
    fun getCards(): Flow<List<Card>> = flow {
        emit(
            getTestCardCollection()
        )
    }

    @Query("SELECT * FROM cards WHERE quantity > 0 ORDER BY name ASC")
    fun getOwnedCards(): Flow<List<Card>> = flow {
        emit(
            getTestCardCollection()
        )
    }

    @Query("SELECT * FROM cards WHERE quantity > 0 ORDER BY price DESC LIMIT 5")
    fun getTopOwnedCardsByPrice(): Flow<List<Card>> = flow {
        emit(
            getTestCardCollection().sortedByDescending { it.price }.take(5)
        )
    }
}

private fun getTestCardCollection(): List<Card> {
    return listOf(
        Card(
            1,
            "Lightning Bolt",
            "2X2",
            117,
            R.drawable.card_lightning_bolt_2x2_117,
            2.30,
            4
        ),
        Card(
            1,
            "Lightning Bolt",
            "CLB",
            187,
            R.drawable.card_lightning_bolt_clb_187,
            1.18,
            2
        ),
        Card(
            1,
            "Humility",
            "TPR",
            16,
            R.drawable.card_humility_tpr_16,
            36.76,
            1
        ),
        Card(
            1,
            "Horizon Canopy",
            "IMA",
            240,
            R.drawable.card_horizon_canopy_ima_240,
            5.25,
            4
        ),
        Card(
            1,
            "Thalia's Lancers",
            "EMN",
            47,
            R.drawable.card_thalia_s_lancers_emn_47,
            0.45,
            3
        )
    )
}