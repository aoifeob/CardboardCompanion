package com.example.cardboardcompanion.data.sevice

import com.example.cardboardcompanion.model.card.ScryfallCard
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ScryfallApiService {
    @GET("cards/{set}/{collectorNo}")
    suspend fun getCard(
        @Path("set") set: String,
        @Path("collectorNo") collectorNo: String
    ): Response<ScryfallCard?>
}