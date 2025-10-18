package com.example.practica1moviles.presentation.fitness

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica1moviles.ui.theme.GradientBackground
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessTrackerScreen(
    onNavigateBack: () -> Unit
) {
    var selectedActivity by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var selectedIntensity by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val activityOptions = listOf(
        "Correr" to 10.0,
        "Caminar" to 5.0,
        "Nadar" to 8.0,
        "Ciclismo" to 7.0,
        "Yoga" to 4.0
    )

    val intensityOptions = listOf(
        "Baja" to 0.8,
        "Media" to 1.0,
        "Alta" to 1.2
    )

    fun calculateCalories() {
        if (selectedActivity.isBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Por favor, selecciona un tipo de actividad",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        if (duration.isBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Por favor, ingresa la duración en minutos",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        if (selectedIntensity.isBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Por favor, selecciona la intensidad",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        val durationValue = duration.toIntOrNull()
        if (durationValue == null) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "La duración debe ser un número entero válido",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        if (durationValue <= 0) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "La duración debe ser un número positivo",
                    duration = SnackbarDuration.Long
                )
            }
            return
        }

        val caloriesPerMinute = activityOptions.find { it.first == selectedActivity }?.second ?: 0.0
        val intensityFactor = intensityOptions.find { it.first == selectedIntensity }?.second ?: 1.0
        val caloriesBurned = caloriesPerMinute * durationValue * intensityFactor
        val formattedCalories = String.format(Locale.getDefault(), "%.2f", caloriesBurned)

        resultMessage = "Has quemado aproximadamente $formattedCalories calorías realizando $selectedActivity durante $durationValue minutos con intensidad $selectedIntensity"
        showResult = true
    }

    GradientBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Registro de Actividad Física",
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        actionColor = MaterialTheme.colorScheme.primary
                    )
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏃 Calcula las calorías quemadas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Card contenedor de inputs
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Dropdown de tipo de actividad
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedActivity,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Tipo de Actividad", style = MaterialTheme.typography.bodyLarge) },
                                placeholder = { Text("Selecciona una actividad") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                activityOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.first) },
                                        onClick = {
                                            selectedActivity = option.first
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Campo de duración
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            label = { Text("Duración (en minutos)", style = MaterialTheme.typography.bodyLarge) },
                            placeholder = { Text("Ej: 30") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            supportingText = {
                                Text(
                                    "Debe ser un número entero positivo",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        // RadioButtons para intensidad
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Intensidad",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectableGroup()
                            ) {
                                intensityOptions.forEach { option ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .selectable(
                                                selected = (selectedIntensity == option.first),
                                                onClick = { selectedIntensity = option.first },
                                                role = Role.RadioButton
                                            )
                                            .padding(horizontal = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = (selectedIntensity == option.first),
                                            onClick = null,
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = MaterialTheme.colorScheme.primary,
                                                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                        Text(
                                            text = option.first,
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = if (selectedIntensity == option.first)
                                                MaterialTheme.colorScheme.onSurface
                                            else
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de calcular
                Button(
                    onClick = { calculateCalories() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Calcular Calorías",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                // Mostrar resultado
                if (showResult) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "🔥",
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "Resultado",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Text(
                                text = resultMessage,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
