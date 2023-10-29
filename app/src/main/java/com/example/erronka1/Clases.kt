package com.example.erronka1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.time.LocalDate

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
}

data class Order(
    val order_id: String?,
    val order_date: String?,
    val food_id: String?,
    val user_id: String?,
    val order_price: Double? = null,
    var order_status: Status?
){
    enum class Status(val displayName: String) {
        ordered("ordered"),
        delivered("delivered");

        companion object {
            fun from(findValue: String): Status =
                values().first { it.displayName.equals(findValue, true) }
        }

        override fun toString(): String {
            return displayName
        }
    }
    fun GetStatus(): String{
        return order_status!!.displayName
    }
    fun ChangeStatus() {
        if (order_status == Status.ordered) {
            order_status = Status.delivered
        }
    }

}

data class GSorpresa(
    override val id: String?,
    override val title: String?,
    override val price: Double?
) : Product
interface OnImageDownloadListener {
    fun onImageDownloaded(bitmap: Bitmap)
    fun onDownloadFailed()
}
data class Food(
    override val id: String?,
    override val title: String?,
    override var price: Double?,

    val pic: String?,
    val category: Category?,
    val season: Seasons?
) : Product {
    enum class Seasons(val displayName: String) {
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
    val selectedFoodList: MutableList<Food> = mutableListOf<Food>(),
    val selectedGSorpresa: MutableList<GSorpresa> = mutableListOf<GSorpresa>()
){
    var eventFood: MutableList<MyEventHandler>? = null
    var eventGose: MutableList<MyEventHandler>? = null

    companion object{
        @Volatile
        private var INSTANCE: SelectedFood? = null
        fun getInstance(): SelectedFood{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: SelectedFood().also {INSTANCE = it}
            }
        }
    }
    fun addFood(newFood: Food){
        selectedFoodList.add(newFood)
        eventFood?.forEach { it(selectedFoodList) }
    }
    fun addGSorpresa(newGSorpresa: GSorpresa){
        selectedGSorpresa.add(newGSorpresa)
        eventGose?.forEach { it(selectedFoodList) }
    }

    fun deleteFood(foodToDelete: Food){
        selectedFoodList.remove(foodToDelete)
        eventFood?.forEach { it.invoke(selectedFoodList) }
    }

    fun deleteGSorpresa(sorpresaToDelete: GSorpresa){
        selectedGSorpresa.remove(sorpresaToDelete)
        eventGose?.forEach{it.invoke(selectedFoodList)}
    }

    fun checkFood(foodToCheck: Food):Boolean{
        return selectedFoodList.contains(foodToCheck)
    }

    fun checkGSorpresa (sorpresaToCheck: GSorpresa):Boolean{
        return selectedGSorpresa.contains(sorpresaToCheck)
    }

    fun clearFoodList(){
        selectedFoodList.clear()
        selectedGSorpresa.clear()
    }
}