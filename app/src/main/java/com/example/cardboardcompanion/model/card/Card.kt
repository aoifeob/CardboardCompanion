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
    var id: Long,
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

}

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY name ASC")
    fun getCards(): Flow<List<Card>> = flow {
        emit(
            listOf(
                Card(
                    1,
                    "Lightning Bolt",
                    "2X2",
                    117,
                    R.drawable.card_lightning_bolt_2x2_117,
                    0.13,
                    1
                )
            )
        )
    }

    @Query("SELECT * FROM cards WHERE quantity > 0 ORDER BY name ASC")
    fun getOwnedCards() : Flow<List<Card>>
}

//@HiltViewModel
//class TestVM @Inject constructor(private val repository: CardRepository) : ViewModel() {
//    private val _cards = MutableStateFlow(emptyList<Card>())
//    val cards = _cards.asStateFlow()
//
//    fun getCardList() {
//        viewModelScope.launch(IO) {
//            repository.getCards().collectLatest {
//                _cards.tryEmit(it)
//            }
//        }
//    }
//}
//
//@Composable
//fun TestUI(vm: TestVM = hiltViewModel<TestVM>()) {
//    LaunchedEffect(key1 = true, block = {
//        vm.getCardList()
//    })
//
//
//    val cards by vm.cards.collectAsStateWithLifecycle()
//
//    Text(text = cards.size.toString())
//
//}

data class CardCollection(var collection: List<Card>) {
    fun isEmpty(): Boolean {
        return collection.isEmpty()
    }
}

data class DetectedCard(
    var name: String,
    var set: String,
    var collectorNo: String,
    var price: Double
) {

    fun getDisplayDetails(): String {
        return "$name ($set $collectorNo)"
    }

    fun getDisplayPrice(): String {
        return "€$price"
    }
}