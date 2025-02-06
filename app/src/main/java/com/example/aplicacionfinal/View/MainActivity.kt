package com.example.aplicacionfinal.View

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacionfinal.Controller.ImageController
import com.example.aplicacionfinal.R
import com.example.aplicacionfinal.ui.theme.AplicacionFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = ImageController()

        setContent {
            AplicacionFinalTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavigationComponent(navController = navController, controller = controller)
                }
            }
        }
    }
}

@Composable
fun NavigationComponent(navController: NavHostController, controller: ImageController) {
    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") { MenuScreen(navController, controller) }
        composable("buscarImagenes/{searchQuery}") { backStackEntry ->
            val searchQuery = backStackEntry.arguments?.getString("searchQuery") ?: ""
            ImageSearchScreen(controller = controller, navController = navController, searchQuery = searchQuery)
        }
        composable("imagenesSeleccionadas") { SelectedImagesScreen(controller) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavHostController, controller: ImageController) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Búsqueda de Imágenes") },
                navigationIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Naturaleza") },
                            onClick = {
                                val query = "nature"
                                navController.navigate("buscarImagenes/$query")
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Animales") },
                            onClick = {
                                val query = "animals"
                                navController.navigate("buscarImagenes/$query")
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Tecnología") },
                            onClick = {
                                val query = "technology"
                                navController.navigate("buscarImagenes/$query")
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Arte") },
                            onClick = {
                                val query = "art"
                                navController.navigate("buscarImagenes/$query")
                                expanded = false
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Image(
                painter = painterResource(id = R.drawable.fondo1),
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val query = " "
                        navController.navigate("buscarImagenes/$query")
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar imágenes",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Buscar imágenes", fontSize = 18.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val selectedImages = controller.getSelectedImages()
                        if (selectedImages.isEmpty()) {
                            Toast.makeText(context, "No hay imágenes seleccionadas", Toast.LENGTH_SHORT).show()
                        } else {
                            navController.navigate("imagenesSeleccionadas")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF58A5F0))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ver Imágenes Seleccionadas",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Ver Imágenes Seleccionadas", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
