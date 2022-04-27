package com.tuan.tracker_data.mapper

import com.tuan.tracker_data.remote.dto.Product
import com.tuan.tracker_domain.model.TrackableFood
import kotlin.math.roundToInt

fun Product.toTrackableFood(): TrackableFood? {

    val carbPer100g = nutriments.carbohydrates100g.roundToInt()
    val proteinPer100g = nutriments.proteins100g.roundToInt()
    val fatPer100g = nutriments.fat100g.roundToInt()
    val caloriesPer100g = nutriments.energyKcal100g.roundToInt()

    return TrackableFood(
        name = productName ?: return null,
        caloriesPer100g = caloriesPer100g,
        proteinPer100g = proteinPer100g,
        fatPer100g = fatPer100g,
        carbPer100g = carbPer100g,
        imageUrl = imageFrontThumbUrl
    )
}