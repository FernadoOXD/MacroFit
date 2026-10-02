package com.upchiapas.kt_template.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upchiapas.kt_template.viewmodel.NutriFitViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upchiapas.kt_template.model.Gender
import com.upchiapas.kt_template.model.Goal
import com.upchiapas.kt_template.ui.components.MacroResultCard
import com.upchiapas.kt_template.ui.components.TotalCaloriesCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: NutriFitViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora de Macros") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )

        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.age,
                        onValueChange = viewModel::onAgeChanged,
                        label = {Text("Edad")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = uiState.weight,
                        onValueChange = viewModel::onWeightChanged,
                        label = {Text("Peso (kg)")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = uiState.height,
                        onValueChange = viewModel::onHeightChanged,
                        label = {Text("Altura (cm)")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                }
            }

            item {
                Text(text = "Genero Biologico", style = MaterialTheme.typography.labelLarge)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = uiState.gender == Gender.MALE,
                        onClick = { viewModel.onGenderChanged(Gender.MALE)},
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) { Text("Hombre") }
                    SegmentedButton(
                        selected = uiState.gender == Gender.FEMALE,
                        onClick = { viewModel.onGenderChanged(Gender.FEMALE)},
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) { Text("Mujer") }
                }
            }
            item {
                Text(text = "Nivel de Actividad Fisica", style = MaterialTheme.typography.labelLarge)
                Slider(
                    value = uiState.activityLevel,
                    onValueChange = viewModel::onActivityLevelChanged,
                    valueRange = 1.2f..1.9f,
                    steps = 3
                )
            }

            item {
                Text(text = "Onjetivo", style = MaterialTheme.typography.labelLarge)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = uiState.goal == Goal.LOSE,
                        onClick = { viewModel.onGoalChanged(Goal.LOSE)},
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                    ) { Text("Perder") }
                    SegmentedButton(
                        selected = uiState.goal == Goal.MAINTAIN,
                        onClick = { viewModel.onGoalChanged(Goal.MAINTAIN)},
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                    ) { Text("Mantener") }
                    SegmentedButton(
                        selected = uiState.goal == Goal.GAIN,
                        onClick = { viewModel.onGoalChanged(Goal.MAINTAIN)},
                        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                    ) { Text("Ganar" )}
                }
            }

            item {
                TotalCaloriesCard(totalCalories = uiState.totalCalories)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val maxGrams = (uiState.proteinGrams + uiState.carbsGram + uiState.fatGrams).toFloat()
                    val proteinProgress = if (maxGrams > 0) uiState.proteinGrams / maxGrams else 0f
                    val carbsProgress = if (maxGrams > 0) uiState.carbsGram / maxGrams else 0f

                    MacroResultCard(
                        title = "Proteinas",
                        amount = uiState.proteinGrams,
                        unit = "g",
                        progress = proteinProgress,
                        indicatorColor = Color(0xFFE57373),
                        modifier = Modifier.weight(1f)
                    )

                    MacroResultCard(
                        title = "Carbohidratos",
                        amount = uiState.carbsGram,
                        unit = "g",
                        progress = carbsProgress,
                        indicatorColor = Color(0xFFFFB74D),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                val maxGrams = (uiState.proteinGrams + uiState.carbsGram + uiState.fatGrams).toFloat()
                val fatProgress = if (maxGrams > 0) uiState.fatGrams / maxGrams else 0f

                MacroResultCard(
                    title = "Grasas (Lipidos)",
                    amount = uiState.fatGrams,
                    unit = "g",
                    progress = fatProgress,
                    indicatorColor = Color(0xFF81C784),
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
            }
        }
    }
}

