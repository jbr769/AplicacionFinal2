package com.example.aplicacionfinal.Controller

import androidx.compose.runtime.mutableStateListOf
import com.example.aplicacionfinal.Model.ImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class ImageController {

    private val client = OkHttpClient()
    private val selectedImages = mutableStateListOf<ImageModel>()

    suspend fun searchImages(query: String): List<ImageModel> {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://pixabay.com/api/?key=48639495-04f8d6bae4f0cb71303d4761b&q=$query&image_type=photo"

                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    throw IOException("Error en la solicitud: ${response.code}")
                }

                val responseData = response.body?.string() ?: throw IOException("Respuesta vacía del servidor")
                val jsonObject = JSONObject(responseData)
                val hits = jsonObject.getJSONArray("hits")

                val images = mutableListOf<ImageModel>()
                for (i in 0 until hits.length()) {
                    val hit = hits.getJSONObject(i)
                    val imageUrl = hit.getString("webformatURL")
                    images.add(ImageModel(imageUrl))
                }
                images
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    fun addImageToSelection(image: ImageModel) {
        if (!selectedImages.contains(image)) {
            selectedImages.add(image)
        }
    }

    fun removeImageFromSelection(image: ImageModel) {
        selectedImages.remove(image)
    }

    fun getSelectedImages(): List<ImageModel> {
        return selectedImages
    }

    fun getSelectedImagesCount(): Int {
        return selectedImages.size
    }
}
