package com.upchiapas.kt_template.viewmodel

import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.upchiapas.kt_template.model.Gender
import com.upchiapas.kt_template.model.Goal
import com.upchiapas.kt_template.model.NutriFitUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

class NutriFitViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NutriFitUiState())
    val uiState: StateFlow<NutriFitUiState> = _uiState.asStateFlow()

    fun onAgeChanged(newAge: String){
        _uiState.update { it.copy(age = newAge) }
        calculateMacros()
    }

    fun onWeightChanged(newWeight: String){
        _uiState.update { it.copy( weight = newWeight) }
        calculateMacros()
    }

    fun onHeightChanged(newHeight: String){
        _uiState.update { it.copy( height = newHeight) }
        calculateMacros()
    }

    fun onGenderChanged(newGender: Gender){
        _uiState.update { it.copy( gender = newGender) }
        calculateMacros()
    }

    fun onActivityLevelChanged(newActivityLevel: Float){
        _uiState.update { it.copy( activityLevel = newActivityLevel) }
        calculateMacros()
    }

    fun onGoalChanged(newGoal: Goal){
        _uiState.update { it.copy( goal = newGoal) }
        calculateMacros()
    }

    private fun calculateMacros(){
        val currentState = _uiState.value

        val weight = currentState.weight.toDoubleOrNull() ?:0.0
        val height = currentState.height.toDoubleOrNull() ?:0.0
        val age = currentState.age.toIntOrNull() ?:0

        if ( weight == 0.0 || height == 0.0 || age == 0){
            _uiState.update { it.copy(
                totalCalories = 0,
                proteinGrams = 0,
                carbsGram = 0,
                fatGrams = 0
            ) }
            return
        }

        var bmr = (10 * weight) + (6.25 * height) - (5 * age)
        bmr += if (currentState.gender == Gender.MALE) 5 else -161

        val maintenanceCalories = bmr * currentState.activityLevel

        val finalCalories = when (currentState.goal){
            Goal.LOSE -> maintenanceCalories - 500
            Goal.MAINTAIN -> maintenanceCalories
            Goal.GAIN -> maintenanceCalories + 500
        }

        val protein = weight * 2.0
        val proteinCalories = protein * 4.0

        val fatCalories = finalCalories * 0.25
        val fat = fatCalories / 9.0

        val carbsCalories = finalCalories - proteinCalories - fatCalories
        val carbs = (if (carbsCalories > 0) carbsCalories else 0.0) / 4.0

        _uiState.update { it.copy(
            totalCalories = finalCalories.roundToInt(),
            proteinGrams = protein.roundToInt(),
            carbsGram = carbs.roundToInt(),
            fatGrams = fat.roundToInt()
        ) }
    }
}