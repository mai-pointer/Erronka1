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
    val cantidadRecomendaciones = 5;
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
        //Boton de recomendaciones
        Recomendar()
        findViewById<Button>(R.id.recomendaciones_platos).setOnClickListener{
            Recomendar()
        }

        //ListView de cada categoria de platos platos
        for (boton in botones){
            findViewById<Button>(boton.boton).setOnClickListener{
                val comidaFiltrada = comida.filter { it.category == boton.categoria }

                val adapter = FoodAdapter(this, comidaFiltrada)
                findViewById<ListView>(R.id.lista_platos).adapter = adapter
            }
        }

        //Boton de GSorpresa
        findViewById<Button>(R.id.gose_platos).setOnClickListener{
            val adapter = GSorpresaAdapter(this, sorpresa)
            findViewById<ListView>(R.id.lista_platos).adapter = adapter
        }

    }

    fun Recomendar(){
        val comidaFiltrada = comida.shuffled().take(if (comida.size >= cantidadRecomendaciones) cantidadRecomendaciones else comida.size)
        val adapter = FoodAdapter(this, comidaFiltrada)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }

    data class BotonesInfo(
        var boton: Int,
        var categoria: Food.Category
    )
}

