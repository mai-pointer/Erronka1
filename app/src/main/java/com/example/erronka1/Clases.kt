package com.example.erronka1

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import java.io.File

data class Proveedor(
    var nombre: String,
    var descripcion: String,
    var productos: List<String>,
    var latitud: Double,
    var longitud: Double

)

interface Product {
    val id: String?
    val title: String?
    val price: Double?
    val pic: String?
}

data class Order(
    val id: String?,
    val data: String
){
    var list: String? = null
    var price: Double? = null
}

data class GSorpresa(
    override val id: String?,
    override val title: String?,
    override val price: Double?,
    override val pic: String?
) : Product
interface OnImageDownloadListener {
    fun onImageDownloaded(bitmap: Bitmap)
    fun onDownloadFailed()
}
data class Food(
    override val id: String?,
    override val title: String?,
    override val price: Double?,
    override val pic: String?,

    val category: Category?,
    val season: Seasons?
) : Product {
    enum class Seasons(private val displayName: String) {
        SPRING("spring"),
        SUMMER("summer"),
        AUTUMN("fall"),
        WINTER("winter");

        companion object {
            fun from(findValue: String): Seasons =
                values().first { it.displayName.equals(findValue, true) }
        }

        override fun toString(): String {
            return displayName
        }
    }

    enum class Category(private val displayName: String) {
        STARTER("starter"),
        DESSERT("dessert"),
        MAIN("main");

        companion object {
            fun from(findValue: String): Category =
                values().first { it.displayName.equals(findValue, true) }
        }

        override fun toString(): String {
            return displayName
        }
    }

    fun downloadImageFromCloudStorage(completion: (Bitmap?) -> Unit) {
        val localFile = File.createTempFile("images", "jpg")
        pic?.let { imageUrl ->
            val imageRef = FirebaseStorage.getInstance().reference.child("images/$imageUrl")

            imageRef.getFile(localFile).addOnSuccessListener {
                if (localFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    completion(bitmap)
                } else {
                    completion(null)
                }
            }.addOnFailureListener { exception ->
                // Manejo de errores de Firebase
                Log.e("FirebaseError", "Error al descargar la imagen: ${exception.message}")
                completion(null)
            }
        } ?: completion(null)
    }
}

typealias MyEventHandler = (MutableList<Food>) -> Unit
class SelectedFood private constructor(
    val selectedFoodList: MutableList<Food> = mutableListOf<Food>()
){
    var eventHandlers: MutableList<MyEventHandler>? = null

    fun setEventHandler(handlers: MutableList<MyEventHandler>?) {
        eventHandlers = handlers
    }
    companion object{
        @Volatile
        private var INSTANCE: SelectedFood? = null

        fun getInstance(): SelectedFood{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: SelectedFood().also {INSTANCE = it}
            }
        }
    }
    public fun addFood(newFood: Food){
        selectedFoodList.add(newFood)
        eventHandlers?.forEach { it(selectedFoodList) }
    }

    public fun deleteFood(foodToDelete: Food){
        selectedFoodList.remove(foodToDelete)
        eventHandlers?.forEach { it.invoke(selectedFoodList) }
    }

    public fun checkFood(foodToCheck: Food):Boolean{
        return selectedFoodList.contains(foodToCheck)
    }
}