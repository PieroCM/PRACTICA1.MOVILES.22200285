package com.example.practica1moviles.presentation.sportscars

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.practica1moviles.ui.theme.GradientBackground
import com.example.practica1moviles.ui.theme.PlaceholderDark
import java.text.NumberFormat
import java.util.Locale

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

    val total = autos.sumOf { it.precioUsd }

    GradientBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Catálogo de Autos Deportivos",
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
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(autos) { auto ->
                    AutoCard(auto = auto)
                }

                item {
                    TotalCard(total = total)
                }
            }
        }
    }
}

@Composable
fun AutoCard(auto: Auto) {
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(PlaceholderDark)
            ) {
                AsyncImage(
                    model = auto.imageUrl,
                    contentDescription = "Foto de ${auto.marca} ${auto.modelo}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "${auto.marca} ${auto.modelo}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = auto.marca.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                val formatter = NumberFormat.getCurrencyInstance(Locale.US)
                Text(
                    text = formatter.format(auto.precioUsd),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "💰 Costo total de los autos:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
            Text(
                text = formatter.format(total),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
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

