package com.example.practica1moviles.presentation.sportscars

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.text.NumberFormat
import java.util.Locale

// Data class para representar un auto deportivo
data class Auto(
    val marca: String,
    val modelo: String,
    val precioUsd: Double,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsCarsScreen(
    onNavigateBack: () -> Unit = {}
) {
    // Mock data - Lista de autos deportivos
    val autos = listOf(
        Auto(
            marca = "Mitsubishi",
            modelo = "Lancer Evolution V",
            precioUsd = 45000.0,
            imageUrl = "https://www.fastcar.co.uk/wp-content/uploads/sites/2/Modified-Evo-V-1.jpg?w=900"
        ),
        Auto(
            marca = "Subaru",
            modelo = "Impreza WRX STI WRC 2004",
            precioUsd = 120000.0,
            imageUrl = "https://p.turbosquid.com/ts-thumb/ET/NevA1T/Au/subaru_impreza_sti_wrc_2004_0000/jpg/1709224897/600x600/fit_q87/95450e203df1dd4c58df691aecb65821d5f50300/subaru_impreza_sti_wrc_2004_0000.jpg"
        ),
        Auto(
            marca = "BMW",
            modelo = "M4 (G82) Widebody",
            precioUsd = 78000.0,
            imageUrl = "https://cdn.bmwblog.com/wp-content/uploads/2020/10/g82-bmw-m4-widebody-03.jpg"
        ),
        Auto(
            marca = "Mazda",
            modelo = "RX-7 Veilside (Tokyo Drift)",
            precioUsd = 70000.0,
            imageUrl = "https://e1.pxfuel.com/desktop-wallpaper/224/395/desktop-wallpaper-turbo-drift-mazda-japan-jdm-rx7-tuning-hawks-the-fast-and-the-furious-tokyo-fast-furious-veilside-section-mazda-fast-and-furious-mazda.jpg"
        ),
        Auto(
            marca = "Toyota",
            modelo = "AE86 Trueno",
            precioUsd = 25000.0,
            imageUrl = "https://p.turbosquid.com/ts-thumb/3f/WdaIZd/g2CePslb/ae86_01/jpg/1382525352/600x600/fit_q87/1109a77d71bfc715413bf795d06d4a961cd4912a/ae86_01.jpg"
        )
    )

    // Calcular el total de todos los autos
    val total = autos.sumOf { it.precioUsd }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Autos Deportivos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Renderizar cada auto en una Card
            items(autos) { auto ->
                AutoCard(auto = auto)
            }

            // Mostrar el total al final
            item {
                TotalCard(total = total)
            }
        }
    }
}

@Composable
fun AutoCard(auto: Auto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Imagen del auto
            AsyncImage(
                model = auto.imageUrl,
                contentDescription = "Foto de ${auto.marca} ${auto.modelo}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop,
                placeholder = null,
                error = null
            )

            // Información del auto
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Título: Marca + Modelo
                Text(
                    text = "${auto.marca} ${auto.modelo}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Marca como subtítulo
                Text(
                    text = auto.marca.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Precio formateado en USD
                val formatter = NumberFormat.getCurrencyInstance(Locale.US)
                Text(
                    text = formatter.format(auto.precioUsd),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun TotalCard(total: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "💰 Costo total de los autos:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
            Text(
                text = formatter.format(total),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SportsCarsScreenPreview() {
    MaterialTheme {
        SportsCarsScreen()
    }
}

