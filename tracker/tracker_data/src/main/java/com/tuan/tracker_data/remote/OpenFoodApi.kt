package com.tuan.tracker_data.remote

import com.tuan.tracker_data.remote.dto.SearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFoodApi {

    /**
     * Search for foods. Only take 3 fields: name, nutrients and front image of the product
     *
     * @param query the food name
     * @param page the page to navigate
     * @param pageSize defines how many foods are shown per page
     */
    @GET(value = SEARCH_URL)
    suspend fun searchFood(
        @Query(value = "search_terms") query: String,
        @Query(value = "page") page: Int,
        @Query(value = "page_size") pageSize: Int
    ): SearchDto

    companion object {
        private const val SEARCH_URL =
            "cgi/search.pl?search_simple=1&json=1&action=process&fields=product_name,nutriments,image_front_thumb_url"
        const val BASE_URL = "https://us.openfoodfacts.org/"
    }
}