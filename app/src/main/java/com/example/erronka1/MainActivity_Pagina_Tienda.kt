package com.example.erronka1

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class MainActivity_Pagina_Tienda : AppCompatActivity() {

    /*val sorpresa1 = GSorpresa(
        id = 1,
        title = "Regalo Sorpresa 1",
        desc = "Un regalo sorpresa emocionante",
        price = 9.99,
        pic = "sorpresa1.jpg"
    )

    val sorpresa2 = GSorpresa(
        id = 2,
        title = "Caja Misteriosa",
        desc = "¡Nunca sabrás lo que hay dentro!",
        price = 14.99,
        pic = "misterio.jpg"
    )*/

    // Objetos Food
    val comida = mutableListOf<Food>()

    val user = FirebaseAuth.getInstance().currentUser
    val botones = listOf(
        BotonesInfo(R.id.primeros_platos, Food.Category.MAIN),
        BotonesInfo(R.id.entrantes_platos, Food.Category.STARTER)
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pagina_tienda)

        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db")

        /*myRef.child("food_01").child("food_id").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }*/


        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (foodSnapshot in dataSnapshot.children) {
                    val foodId = foodSnapshot.child("food_id").getValue(String::class.java)
                    val foodCategory = foodSnapshot.child("food_category").getValue(String::class.java)
                    val foodDesc = foodSnapshot.child("food_desc").getValue(String::class.java)
                    val foodPic = foodSnapshot.child("food_pic").getValue(String::class.java)
                    val foodPrice = foodSnapshot.child("food_price").getValue(Double::class.java)
                    val foodSeason = foodSnapshot.child("food_season").getValue(String::class.java)
                    val foodTitle = foodSnapshot.child("food_title").getValue(String::class.java)

                    Log.d(TAG, "Food ID: $foodId")
                    Log.d(TAG, "Food Category: $foodCategory")
                    Log.d(TAG, "Food Description: $foodDesc")
                    Log.d(TAG, "Food Picture: $foodPic")
                    Log.d(TAG, "Food Price: $foodPrice")
                    Log.d(TAG, "Food Season: $foodSeason")
                    Log.d(TAG, "Food Title: $foodTitle")

                    val food = Food(foodId, foodTitle, foodDesc, foodPrice, foodPic, Food.Category.from(foodCategory.toString()), Food.Seasons.from(foodSeason.toString()))
                    comida.add(food)
                }


                for (boton in botones){
                    findViewById<Button>(boton.boton).setOnClickListener{
                        cargarList(boton.categoria)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error getting data", error.toException())
            }
        })
    }

    fun cargarList(categoria : Food.Category){
        val comidaFiltrada = comida.filter { it.category == categoria }

        val adapter = FoodAdapter(this, comidaFiltrada)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }
}

class FoodAdapter(private val context: Context, private val foodList: List<Food>) : BaseAdapter() {
    //Funciones del adaptador
    override fun getCount(): Int {
        return foodList.size
    }
    override fun getItem(position: Int): Any {
        return foodList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //Edita la view
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //Declara la view y el alamcen de los datos
        val view: View
        val elementos: Elementos

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.platos_tienda, parent, false)
            elementos = Elementos() // declara la clase

            // Obtener las vistas del diseño
            elementos.textTitle = view.findViewById(R.id.titulo_platos)
            elementos.textDescription = view.findViewById(R.id.descripcion_platos)
            elementos.textPrice = view.findViewById(R.id.precio_platos)
            elementos.imageFood = view.findViewById(R.id.imagen_platos)
            elementos.buttonAdd = view.findViewById(R.id.boton_platos)

            view.tag = elementos
        } else {
            view = convertView
            elementos = view.tag as Elementos
        }

        // Obtener el objeto Food en esta posición
        val food = foodList[position]

        // Establecer los valores en las vistas
        elementos.textTitle.text = food.title
        elementos.textDescription.text = food.desc
        elementos.textPrice.text = "${context.getString(R.string.precio)}: ${food.price} €"
        // *** Aquí debes cargar la imagen en holder.imageFood ***
        elementos.buttonAdd.setOnClickListener {
            // *** Agregar lógica para manejar el botón "Añadir" ***
        }

        return view
    }

    //Clase para guardar los elementos de la view
    private class Elementos {
        lateinit var textTitle: TextView
        lateinit var textDescription: TextView
        lateinit var textPrice: TextView
        lateinit var imageFood: ImageView
        lateinit var buttonAdd: Button
    }
}

data class BotonesInfo(
    var boton: Int,
    var categoria: Food.Category
)