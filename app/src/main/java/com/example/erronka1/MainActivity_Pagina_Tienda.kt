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
    var goseList= mutableListOf<GSorpresa>()

    val user = FirebaseAuth.getInstance().currentUser
    val botones = listOf(
        BotonesInfo(R.id.entrantes_platos, "starter"),
        BotonesInfo(R.id.principal_platos, "main"),
        BotonesInfo(R.id.gose_platos, "gose")

    )
    var myCurrentCategory = "starter"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pagina_tienda)

        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        selectedFoodList = SelectedFood.getInstance()
        if (selectedFoodList.eventFood == null) {
            selectedFoodList.eventFood = mutableListOf()
        }
        if (selectedFoodList.eventGose == null) {
            selectedFoodList.eventGose = mutableListOf()
        }
        selectedFoodList.eventFood?.add {
            CargarLista(myCurrentCategory!!)
        }
        selectedFoodList.eventGose?.add {
            CargarGose(goseList)
        }

        BD.GetFood(this){foods ->
            foods.forEach{food ->
                comida.add(food)
            }
            for (boton in botones){
                findViewById<Button>(boton.boton).setOnClickListener{
                    myCurrentCategory = boton.categoria
                    CargarLista(myCurrentCategory!!)
                }
            }

            //Boton de GSorpresa
            BD.GetGose(this) {gose ->
                gose.forEach { gosebat ->
                    goseList.add(gosebat)
                }
                findViewById<Button>(R.id.gose_platos).setOnClickListener{
                    CargarGose(goseList)
                    myCurrentCategory = "gose"
                }
            }

            if(myCurrentCategory == "gose"){
                CargarGose(goseList)
            }else{
                CargarLista(myCurrentCategory!!)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        selectedFoodList = SelectedFood.getInstance()
        selectedFoodList.eventFood?.remove{
            CargarLista(myCurrentCategory!!)

        }
        selectedFoodList.eventGose?.remove {
            CargarGose(goseList)
        }
    }

    fun CargarGose(gose: List<GSorpresa>){
        val adapter = GSorpresaAdapter(this, gose, selectedFoodList)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }

    fun CargarLista(categoria: String){
        val comidaFiltrada = comida.filter { it.category.toString() == categoria }

        val selectedFoodList = SelectedFood.getInstance()

        val adapter = FoodAdapter(this, comidaFiltrada, selectedFoodList)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }

    data class BotonesInfo(
        var boton: Int,
        var categoria: String
    )
}

