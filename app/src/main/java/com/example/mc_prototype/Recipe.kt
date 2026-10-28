package com.example.mc_prototype

import java.util.Locale
import java.util.UUID
import kotlin.math.roundToLong

data class Ingredient(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val amountGrams: Double,
)

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val photoUri: String,
    val ingredients: List<Ingredient>,
)

data class RecipeNutrition(
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val totalWeightGrams: Double,
)

fun calculateRecipeNutrition(ingredients: List<Ingredient>): RecipeNutrition {
    val totalWeight = ingredients.sumOf { it.amountGrams }
    if (totalWeight <= 0.0) {
        return RecipeNutrition(
            caloriesPer100g = 0.0,
            proteinPer100g = 0.0,
            totalWeightGrams = 0.0,
        )
    }

    val totalCalories = ingredients.sumOf { (it.caloriesPer100g * it.amountGrams) / 100.0 }
    val totalProtein = ingredients.sumOf { (it.proteinPer100g * it.amountGrams) / 100.0 }

    val caloriesPer100g = (totalCalories * 100.0) / totalWeight
    val proteinPer100g = (totalProtein * 100.0) / totalWeight

    return RecipeNutrition(
        caloriesPer100g = caloriesPer100g,
        proteinPer100g = proteinPer100g,
        totalWeightGrams = totalWeight,
    )
}

fun formatCalories(caloriesPer100g: Double): String {
    val rounded = caloriesPer100g.roundToLong()
    return "$rounded kcal / 100 g"
}

fun formatProtein(proteinPer100g: Double): String {
    return String.format(Locale.US, "%.1f g protein / 100 g", proteinPer100g)
}
