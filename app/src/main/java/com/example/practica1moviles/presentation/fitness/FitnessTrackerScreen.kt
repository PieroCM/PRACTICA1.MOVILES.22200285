package com.example.practica1moviles.presentation.fitness

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Definir actividades con sus calorías por minuto
    val activityOptions = listOf(
        "Correr" to 10.0,
        "Caminar" to 5.0,
        "Nadar" to 8.0,
        "Ciclismo" to 7.0,
        "Yoga" to 4.0
    )

    // Definir intensidades con sus factores
    val intensityOptions = listOf(
        "Baja" to 0.8,
        "Media" to 1.0,
        "Alta" to 1.2
    )

    // Función para validar y calcular calorías
    fun calculateCalories() {
        // Validar que se haya seleccionado una actividad
        if (selectedActivity.isBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Por favor, selecciona un tipo de actividad",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        // Validar que se haya ingresado la duración
        if (duration.isBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Por favor, ingresa la duración en minutos",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        // Validar que se haya seleccionado una intensidad
        if (selectedIntensity.isBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Por favor, selecciona la intensidad",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        // Validar que la duración sea un número entero válido
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

        // Validar que la duración sea positiva
        if (durationValue <= 0) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "La duración debe ser un número positivo",
                    duration = SnackbarDuration.Long
                )
            }
            return
        }

        // Obtener las calorías por minuto de la actividad seleccionada
        val caloriesPerMinute = activityOptions.find { it.first == selectedActivity }?.second ?: 0.0

        // Obtener el factor de intensidad
        val intensityFactor = intensityOptions.find { it.first == selectedIntensity }?.second ?: 1.0

        // Aplicar la fórmula: calorías = calorías por minuto * duración * factor intensidad
        val caloriesBurned = caloriesPerMinute * durationValue * intensityFactor

        // Formatear resultado a 2 decimales
        val formattedCalories = String.format(Locale.getDefault(), "%.2f", caloriesBurned)

        // Mostrar resultado
        resultMessage = "Has quemado aproximadamente $formattedCalories calorías realizando $selectedActivity durante $durationValue minutos con intensidad $selectedIntensity"
        showResult = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Actividad Física") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏃 Calcula las calorías quemadas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Dropdown de tipo de actividad
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedActivity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Actividad") },
                    placeholder = { Text("Selecciona una actividad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de duración
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duración (en minutos)") },
                placeholder = { Text("Ej: 30") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = { Text("Debe ser un número entero positivo") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // RadioButtons para intensidad
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Intensidad",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
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
                                onClick = null
                            )
                            Text(
                                text = option.first,
                                modifier = Modifier.padding(start = 8.dp),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de calcular
            Button(
                onClick = { calculateCalories() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = "Calcular Calorías",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Mostrar resultado
            if (showResult) {
                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔥 Resultado",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = resultMessage,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}
