package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.annotation.SuppressLint
import android.widget.Button
import android.widget.ListView
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
    val cantidadRecomendaciones = 2;
    val botones = listOf(
        BotonesInfo(R.id.primeros_platos, Food.Category.MAIN),
        BotonesInfo(R.id.entrantes_platos, Food.Category.STARTER)
    )
    var myCurrentCategory: Food.Category? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pagina_tienda)

        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db")

        //Cargar de la base de datos
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (foodSnapshot in dataSnapshot.children) {
                    val foodId = foodSnapshot.child("food_id").getValue(String::class.java)
                    val foodCategory = foodSnapshot.child("food_category").getValue(String::class.java)
                    val foodDesc = foodSnapshot.child("food_desc").getValue(String::class.java)
                    val foodPic = foodSnapshot.child("food_pic").getValue(String::class.java)
                    val foodPrice = foodSnapshot.child("food_price").getValue(Double::class.java)
                    val foodSeason = foodSnapshot.child("food_season").getValue(String::class.java)
                    val foodSelected = foodSnapshot.child("food_selected").getValue(Boolean::class.java)
                    val foodTitle = foodSnapshot.child("food_title").getValue(String::class.java)

                    val food = Food(foodId, foodTitle, foodDesc, foodPrice, foodPic, foodSelected, Food.Category.from(foodCategory.toString()), Food.Seasons.from(foodSeason.toString()))
                    comida.add(food)
                }


                for (boton in botones){
                    findViewById<Button>(boton.boton).setOnClickListener{
                        CargarLista(boton.categoria)
                        myCurrentCategory = boton.categoria
                    }
                }
                if (myCurrentCategory != null){
                    CargarLista(myCurrentCategory!!)
                } else {
                    Recomendar()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error getting data", error.toException())
            }
        })
        //Boton de recomendaciones
        findViewById<Button>(R.id.recomendaciones_platos).setOnClickListener{
            Recomendar()
        }

        //Boton de GSorpresa
        findViewById<Button>(R.id.gose_platos).setOnClickListener{
//            val adapter = GSorpresaAdapter(this, sorpresa)
//            findViewById<ListView>(R.id.lista_platos).adapter = adapter
        }

    }

    fun CargarLista(categoria: Food.Category){
        val comidaFiltrada = comida.filter { it.category == categoria }

        val adapter = FoodAdapter(this, comidaFiltrada)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }

    //Boton de Recomendados
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

