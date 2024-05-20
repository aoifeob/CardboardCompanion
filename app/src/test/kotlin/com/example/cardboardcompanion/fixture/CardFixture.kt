package com.example.cardboardcompanion.fixture

import com.example.cardboardcompanion.model.card.Card
import com.example.cardboardcompanion.model.card.ScryfallCard
import com.example.cardboardcompanion.model.card.ScryfallImageSource
import com.example.cardboardcompanion.model.card.ScryfallPrice

const val testName = "Take Up the Shield"

const val testSet = "otj"

const val testCollectorNo = "34"

const val testPrice = 0.07

val testScryfallCard = ScryfallCard(
    "Take Up the Shield",
    testSet,
    testCollectorNo,
    ScryfallPrice(0.07),
    ScryfallImageSource("https://cards.scryfall.io/normal/front/7/6/76a31968-ba6d-4c01-838f-4cb8c64e73fb.jpg?1712355363")
)

fun getTestScryfallCard(
    name: String = testName,
    set: String = testSet,
    collectorNo: String = testCollectorNo,
    price: Double = testPrice
) = ScryfallCard(
    name,
    set,
    collectorNo,
    ScryfallPrice(price),
    ScryfallImageSource("https://cards.scryfall.io/normal/front/7/6/76a31968-ba6d-4c01-838f-4cb8c64e73fb.jpg?1712355363")
)

fun getTestCard(name: String = testName, set: String = testSet, price: Double = testPrice) =
    Card(
        name = name,
        set = set,
        collectorNo = testCollectorNo,
        image = "https://cards.scryfall.io/normal/front/7/6/76a31968-ba6d-4c01-838f-4cb8c64e73fb.jpg?1712355363",
        price = price,
        quantity = 1
    )

val testCollection = listOf(
    Card(
        name = "Take Up the Shield",
        set = "otj",
        collectorNo = "34",
        image = "https://cards.scryfall.io/normal/front/7/6/76a31968-ba6d-4c01-838f-4cb8c64e73fb.jpg?1712355363",
        price = 0.07,
        quantity = 1
    ),
    Card(
        name = "Outlaw Medic",
        set = "otj",
        collectorNo = "23",
        image = "https://cards.scryfall.io/normal/front/2/f/2feaa51e-47fb-4849-b420-ee7278f3489a.jpg?1712355319",
        price = 0.09,
        quantity = 2
    ),
    Card(
        name = "Arcane Signet",
        set = "mkc",
        collectorNo = "223",
        image = "https://cards.scryfall.io/normal/front/e/5/e593bb16-e709-4d92-bf6f-239f6a11f7db.jpg?1706241039",
        price = 0.31,
        quantity = 6
    ),
    Card(
        name = "Agitator Ant",
        set = "clb",
        collectorNo = "776",
        image = "https://cards.scryfall.io/normal/front/e/8/e885f56a-a1e8-4289-bbf5-3b156e53f7db.jpg?1674141839",
        price = 0.15,
        quantity = 1
    ),
    Card(
        name = "Jace, the Mind Sculptor",
        set = "2xm",
        collectorNo = "56",
        image = "https://cards.scryfall.io/normal/front/c/8/c8817585-0d32-4d56-9142-0d29512e86a9.jpg?1598304029",
        price = 21.45,
        quantity = 4
    ),
    Card(
        name = "Temple Garden",
        set = "grn",
        collectorNo = "56",
        image = "https://cards.scryfall.io/normal/front/2/b/2b9b0195-beda-403e-bc27-7ae3be9f318c.jpg?1572894210",
        price = 8.99,
        quantity = 1
    ),
    Card(
        name = "Prismatic Vista",
        set = "mh1",
        collectorNo = "244",
        image = "https://cards.scryfall.io/normal/front/e/3/e37da81e-be12-45a2-9128-376f1ad7b3e8.jpg?1562202585",
        price = 25.68,
        quantity = 1
    ),
    Card(
        name = "Kiki-Jiki, Mirror Breaker",
        set = "j22",
        collectorNo = "79",
        image = "https://cards.scryfall.io/normal/front/0/e/0e6fc996-17ba-4090-bf82-0c2eba93a81e.jpg?1675645267",
        price = 8.19,
        quantity = 4
    ),
    Card(
        name = "Halvar, God of Battle",
        set = "khm",
        collectorNo = "299",
        image = "https://cards.scryfall.io/normal/front/9/7/97502411-5c93-434c-b77b-ceb2c32feae7.jpg?1631045832",
        price = 9.32,
        quantity = 2
    ),
    Card(
        name = "Oko, the Ringleader",
        set = "otj",
        collectorNo = "223",
        image = "https://cards.scryfall.io/normal/front/3/9/396df8d6-e85d-4486-8116-68841b7e1e2e.jpg?1712356176",
        price = 3.38,
        quantity = 1
    ),
)

val testTop5Collection = testCollection.sortedByDescending { it.price }.take(5)
