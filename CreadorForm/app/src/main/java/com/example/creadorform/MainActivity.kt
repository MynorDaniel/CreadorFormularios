package com.example.creadorform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.creadorform.controlador.AnalisisControlador
import com.example.creadorform.dominio.Formulario
import com.example.creadorform.ui.theme.CreadorFormTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreadorFormTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

var entrada = """
    number x = 4
    string f = " g "
    $ y = 5
    $ number z = x + y
    string texto = "hola mundo"
     IF(9){
    
    }
    
""".trimIndent()

@Composable
fun Main(name: String, modifier: Modifier = Modifier) {
    var analisis: AnalisisControlador = AnalisisControlador()
    var formulario: Formulario = analisis.analizarForm(entrada)
    var errores: String = analisis.errores
    println("Errores: $errores")
    Text(
        text = formulario.toString() + "\n" + errores,
        modifier = modifier
    )
}