package com.example.erronka1

import android.content.ContentValues.TAG
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

   /* var Gsorpresa = mutableListOf(
        GSorpresa(
            id = "1",
            title = "Regalo Sorpresa 1",
            desc = "Un regalo sorpresa emocionante",
            price = 9.99,
            pic = "sorpresa1.jpg"
        ),
        GSorpresa(
            id = "2",
            title = "Caja Misteriosa",
            desc = "¡Nunca sabrás lo que hay dentro!",
            price = 14.99,
            pic = "misterio.jpg"
        ))*/

    // Objetos Food
    val comida = mutableListOf<Food>()
    val GoseS =mutableListOf<GSorpresa>()

    val user = FirebaseAuth.getInstance().currentUser
    val cantidadRecomendaciones = 2;
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
        val myRef2: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/goseS_db")
        //Boton comidas
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

                    val food = Food(foodId, foodTitle, foodDesc, foodPrice, foodPic, Food.Category.from(foodCategory.toString()), Food.Seasons.from(foodSeason.toString()))
                    comida.add(food)
                }


                for (boton in botones){
                    findViewById<Button>(boton.boton).setOnClickListener{
                        CargarLista(boton.categoria)
                    }
                }
                Recomendar()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error getting data", error.toException())
            }
        })

        myRef2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (goseSSnapshot in dataSnapshot.children) {
                    val goseSId = goseSSnapshot.child("goseS_id").getValue(String::class.java)
                    val goseSDesc = goseSSnapshot.child("goseS_desc").getValue(String::class.java)
                    val goseSPic = goseSSnapshot.child("goseS_pic").getValue(String::class.java)
                    val goseSPrice = goseSSnapshot.child("goseS_price").getValue(Double::class.java)
                    val goseSTitle = goseSSnapshot.child("goseS_title").getValue(String::class.java)

                    val goseS = GSorpresa(goseSId, goseSTitle, goseSDesc, goseSPrice, goseSPic)
                    GoseS.add(goseS)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja errores, si es necesario.
            }
        })

        //Boton de recomendaciones
        findViewById<Button>(R.id.recomendaciones_platos).setOnClickListener{
            Recomendar()
        }

        //Boton de GSorpresa
        findViewById<Button>(R.id.gose_platos).setOnClickListener{
//            val adapter = GSorpresaAdapter(this, Gsorpresa)
//            findViewById<ListView>(R.id.lista_platos).adapter = adapter
            Gose_Sorpresa()
        }

    }

    fun CargarLista(categoria: Food.Category){
        val comidaFiltrada = comida.filter { it.category == categoria }

        val adapter = FoodAdapter(this, comidaFiltrada)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }

    //Boton de Recomendados
    fun Gose_Sorpresa(){

        val adapter = GSorpresaAdapter(this, GoseS)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
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

