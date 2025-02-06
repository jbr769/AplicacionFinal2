package com.example.aplicacionfinal.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.aplicacionfinal.Controller.ImageController
import com.example.aplicacionfinal.Model.ImageModel
import com.example.aplicacionfinal.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSearchScreen(controller: ImageController, navController: NavController, searchQuery: String) {
    var query by remember { mutableStateOf(searchQuery) }  // Inicializar la búsqueda con el texto pasado
    var images by remember { mutableStateOf(listOf<ImageModel>()) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val selectedImagesCount by remember { derivedStateOf { controller.getSelectedImages().size } }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier.padding(top = 32.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Imágenes seleccionadas",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 16.dp)
                        .clickable {
                            navController.navigate("imagenesSeleccionadas")
                        }
                )

                if (selectedImagesCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .offset(x = 12.dp, y = -6.dp)
                            .drawBehind {
                                drawCircle(Color.Red)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedImagesCount.toString(),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondobuscador),
                contentDescription = "Fondo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar imágenes") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        scope.launch {
                            images = controller.searchImages(query)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Buscar")
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(images) { image ->
                        ImageItem(
                            image = image,
                            onClick = {
                                controller.addImageToSelection(image)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Imagen añadida a la selección",
                                        actionLabel = "Deshacer",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        controller.removeImageFromSelection(image)
                                        Toast.makeText(context, "Acción deshecha", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageItem(image: ImageModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberImagePainter(data = image.url),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
    }
}
