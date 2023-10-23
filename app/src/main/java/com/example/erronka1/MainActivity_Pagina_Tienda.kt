package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.annotation.SuppressLint
import android.widget.Button
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

    val user = FirebaseAuth.getInstance().currentUser
    val cantidadRecomendaciones = 2;
    var myCurrentCategory: Food.Category? = null
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


        BD.GetFood {foods->
            for (boton in botones){
                findViewById<Button>(boton.boton).setOnClickListener{
                    CargarLista(boton.categoria, foods)
                    myCurrentCategory = boton.categoria
                }
            }
            if (myCurrentCategory != null){
                CargarLista(myCurrentCategory!!, foods)
            } else {
                Recomendar(foods)
            }

            //Boton de recomendaciones
            findViewById<Button>(R.id.recomendaciones_platos).setOnClickListener{
                Recomendar(foods)
            }
        }

        //Boton de GSorpresa
        findViewById<Button>(R.id.gose_platos).setOnClickListener{
//            val adapter = GSorpresaAdapter(this, sorpresa)
//            findViewById<ListView>(R.id.lista_platos).adapter = adapter
        }

    }

    fun CargarLista(categoria: Food.Category, food: List<Food>){
        val comidaFiltrada = food.filter { it.category == categoria }

        val adapter = FoodAdapter(this, comidaFiltrada)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }

    //Boton de Recomendados
    fun Recomendar(food: List<Food>){
        val comidaFiltrada = food.shuffled().take(if (food.size >= cantidadRecomendaciones) cantidadRecomendaciones else food.size)
        val adapter = FoodAdapter(this, comidaFiltrada)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }

    data class BotonesInfo(
        var boton: Int,
        var categoria: Food.Category
    )
}

