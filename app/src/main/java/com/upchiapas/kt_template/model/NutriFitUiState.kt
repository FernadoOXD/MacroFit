package com.upchiapas.kt_template.model

enum class Gender { MALE, FEMALE }
enum class Goal { LOSE, MAINTAIN, GAIN }

data class NutriFitUiState(
    val age: String = "",
    val weight: String = "",
    val height: String  = "",

    val gender: Gender = Gender.MALE,
    val activityLevel: Float = 1.2f,
    val goal: Goal = Goal.MAINTAIN,

    val totalCalories: Int = 0,
    val proteinGrams: Int = 0,
    val carbsGram: Int = 0,
    val fatGrams: Int = 0
)