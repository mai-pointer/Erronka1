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

    private lateinit var selectedFoodList: SelectedFood
    // Objetos Food
    var comida = mutableListOf<Food>()

    val user = FirebaseAuth.getInstance().currentUser
    val botones = listOf(
        BotonesInfo(R.id.entrantes_platos, Food.Category.STARTER),
        BotonesInfo(R.id.principal_platos, Food.Category.MAIN)

    )
    var myCurrentCategory: Food.Category? = Food.Category.STARTER

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pagina_tienda)

        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db")

        selectedFoodList = SelectedFood.getInstance()
        if (selectedFoodList.eventHandlers == null) {
            selectedFoodList.eventHandlers = mutableListOf()
        }
        selectedFoodList.eventHandlers?.add {
            CargarLista(myCurrentCategory!!)
        }

//        //Cargar de la base de datos
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (foodSnapshot in dataSnapshot.children) {
                    val foodId = foodSnapshot.child("food_id").getValue(String::class.java)
                    val foodCategory = foodSnapshot.child("food_category").getValue(String::class.java)
                    val foodPic = foodSnapshot.child("food_pic").getValue(String::class.java)
                    val foodPrice = foodSnapshot.child("food_price").getValue(Double::class.java)
                    val foodSeason = foodSnapshot.child("food_season").getValue(String::class.java)

                    val food = Food(foodId, getString(resources.getIdentifier(foodId, "string", packageName)), foodPrice, foodPic, Food.Category.from(foodCategory.toString()), Food.Seasons.from(foodSeason.toString()))
                    comida.add(food)
                }
                for (boton in botones){
                    findViewById<Button>(boton.boton).setOnClickListener{
                        myCurrentCategory = boton.categoria
                        CargarLista(myCurrentCategory!!)
                    }
                }
                CargarLista(myCurrentCategory!!)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error getting data", error.toException())
            }
        })

        //Boton de GSorpresa
        BD.GetGose {gose ->
            findViewById<Button>(R.id.gose_platos).setOnClickListener{
                val adapter = GSorpresaAdapter(this, gose)
                findViewById<ListView>(R.id.lista_platos).adapter = adapter
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        selectedFoodList = SelectedFood.getInstance()
        selectedFoodList.eventHandlers?.remove{
            CargarLista(myCurrentCategory!!)
        }
    }

    fun CargarLista(categoria: Food.Category){
        val comidaFiltrada = comida.filter { it.category == categoria }

        val selectedFoodList = SelectedFood.getInstance()

        val adapter = FoodAdapter(this, comidaFiltrada, selectedFoodList)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }

    data class BotonesInfo(
        var boton: Int,
        var categoria: Food.Category
    )
}

