package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.appproyectofindegradofranciscodasilva.R


@Composable
fun DualSegmentPieChart(
    data: Map<String, Double>,
    radiusOuter: Dp = 150.dp,
    chartBarWidth: Dp = 25.dp,
    animDuration: Int = 1000,
) {
    // Suma total de los valores del gráfico
    val totalSum = data.values.sum()
    // Lista de valores flotantes que representan los ángulos de los segmentos
    val floatValue = data.values.map { 360 * it.toFloat() / totalSum.toFloat() }
    // Colores para los segmentos del gráfico
    val colors = listOf(Color.Green, Color.Red)

    // Estado mutable para controlar si la animación ha sido reproducida
    var animationPlayed by remember { mutableStateOf(false) }
    var lastValue = 0f

    // Animación del tamaño del gráfico
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        ), label = "animate size"
    )


    // Animación de la rotación del gráfico
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 360f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        ), label = "animate rotation"
    )

    // Efecto lanzado para iniciar la animación
    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.big_size_space)))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2)
                    .rotate(animateRotation)
            ) {
                // Dibuja los segmentos del gráfico
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        startAngle = lastValue,
                        sweepAngle = value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }

        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space)))

        // Muestra los detalles del gráfico
        DetailsPieChart(data = data, colors = colors)
    }
}

@Composable
fun DetailsPieChart(data: Map<String, Double>, colors: List<Color>) {
    Column {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Muestra los detalles de cada segmento
            data.values.forEachIndexed { index, value ->
                DetailsPieChartItem(
                    data = Pair(data.keys.elementAt(index), value),
                    color = colors[index]
                )
            }
        }
    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String, Double>,
    height: Dp = 10.dp,
    color: Color
) {

    Surface(
        modifier = Modifier
            .padding(16.dp),
        color = Color.Transparent
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cuadro de color que representa el segmento
            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .background(
                                color = color,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .align(Alignment.CenterVertically)
                            .size(height)
                    )
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))
                    // Texto con la etiqueta del segmento
                    Text(
                        text = data.first,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_size_space)))
                // Texto con el valor del segmento
                Text(
                    text = String.format("%.2f€", data.second),
                )
            }
        }
    }
}

