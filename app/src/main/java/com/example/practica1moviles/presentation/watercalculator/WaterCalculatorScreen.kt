package com.example.practica1moviles.presentation.watercalculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterCalculatorScreen(
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val genderOptions = listOf(
        "Masculino" to 1.02,
        "Femenino" to 1.01,
        "Sin especificar" to 1.00
    )

    // Función para validar y calcular
    fun calculateWaterIntake() {
        // Validar que todos los campos estén completos
        if (name.isBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Por favor, ingresa tu nombre",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        if (weight.isBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Por favor, ingresa tu peso corporal",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        if (selectedGender.isBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Por favor, selecciona tu género",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        // Validar que el peso sea un número válido
        val weightValue = weight.toDoubleOrNull()
        if (weightValue == null) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "El peso debe ser un número válido",
                    duration = SnackbarDuration.Short
                )
            }
            return
        }

        // Validar rango de peso
        if (weightValue < 5 || weightValue > 200) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "El peso debe estar entre 5 y 200 kg",
                    duration = SnackbarDuration.Long
                )
            }
            return
        }

        // Obtener el factor de género
        val genderFactor = genderOptions.find { it.first == selectedGender }?.second ?: 1.00

        // Aplicar la fórmula: litros = peso * 0.035 * factor_género
        val litersRecommended = weightValue * 0.035 * genderFactor

        // Formatear resultado a 2 decimales
        val formattedLiters = String.format(Locale.getDefault(), "%.2f", litersRecommended)

        // Mostrar resultado
        resultMessage = "$name debe beber aproximadamente $formattedLiters litros de agua al día"
        showResult = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora de Consumo de Agua") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                text = "💧 Calcula tu consumo diario de agua",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo de nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                placeholder = { Text("Ingresa tu nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de peso
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Peso corporal (kg)") },
                placeholder = { Text("Ej: 70") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = { Text("Debe estar entre 5 y 200 kg") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown de género
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    placeholder = { Text("Selecciona tu género") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.first) },
                            onClick = {
                                selectedGender = option.first
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de calcular
            Button(
                onClick = { calculateWaterIntake() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Calcular",
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
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📊 Resultado",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = resultMessage,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}
