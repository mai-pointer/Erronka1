package com.example.erronka1

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity_Pagina_Tienda : AppCompatActivity() {
    // Objetos GSorpresa
    val sorpresa = listOf<GSorpresa>(
//        GSorpresa(
//            id = 1,
//            title = "Caja Misteriosa Individual",
//            desc = "Contiene comida aleatoria para que una persona pueda comer",
//            price = 4.99,
//        ),
//        GSorpresa(
//            id = 2,
//            title = "Caja Misteriosa Familiar",
//            desc = "Contiene comida aleatoria para que cuatro personas puedan comer",
//            price = 14.99,
//        )
    )
    // Objetos Food
    val comida = listOf<Food>(
//        Food(
//            id = 101,
//            title = "Pizza Margarita",
//            desc = "Una deliciosa pizza con tomate, mozzarella y albahaca",
//            price = 10.99,
//            pic = "pizza.jpg",
//            category = Food.Category.MAIN,
//            season = Food.Seasons.SUMMER
//        ),
//        Food(
//            id = 102,
//            title = "Helado de Fresa",
//            desc = "Un postre refrescante perfecto para el verano",
//            price = 4.99,
//            pic = "helado.jpg",
//            category = Food.Category.STARTER,
//            season = Food.Seasons.SUMMER
//        )
    )

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

        MenuNav.Crear(this, user)

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

