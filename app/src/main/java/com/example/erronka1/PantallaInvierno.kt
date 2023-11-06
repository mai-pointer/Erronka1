package com.example.erronka1

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class PantallaInvierno : AppCompatActivity() {

    private lateinit var invierno: ImageView
    private lateinit var otoño: ImageView
    private lateinit var verano: ImageView
    private lateinit var primavera: ImageView
    private lateinit var titulo: TextView
    private lateinit var argazkia: ImageView
    private lateinit var myCurrentSeason: Food.Seasons
    private var comida = mutableListOf<Food>()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invierno)

        invierno = findViewById(R.id.inviernoo)
        otoño = findViewById(R.id.otoñoo)
        verano = findViewById(R.id.veranoo)
        primavera = findViewById(R.id.primaveraa)
        titulo = findViewById(R.id.textview)
        argazkia = findViewById(R.id.imageView4)
        val nuevoTamañoTexto = 30f
        titulo.textSize = nuevoTamañoTexto

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db")

        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        val myseason = intent.extras?.getString("season")
        Log.i("Error-TX", myseason ?: "")
        when (myseason) {
            getString(R.string.I) -> {
                titulo.text=getString(R.string.I)
                invierno.imageAlpha = 255
                primavera.imageAlpha = 100
                verano.imageAlpha = 100
                otoño.imageAlpha = 100
                argazkia.setImageResource(R.drawable.invierno)
                myCurrentSeason = Food.Seasons.WINTER
            }
            getString(R.string.P) -> {
                titulo.text=getString(R.string.P)
                primavera.imageAlpha = 255
                invierno.imageAlpha = 100
                verano.imageAlpha = 100
                otoño.imageAlpha = 100
                argazkia.setImageResource(R.drawable.primavera)
                myCurrentSeason = Food.Seasons.SPRING
            }
            getString(R.string.V) -> {
                titulo.text=getString(R.string.V)
                verano.imageAlpha = 255
                invierno.imageAlpha = 100
                primavera.imageAlpha = 100
                otoño.imageAlpha = 100
                argazkia.setImageResource(R.drawable.verano)
                myCurrentSeason = Food.Seasons.SUMMER
            }
            getString(R.string.O) -> {
                titulo.text=getString(R.string.O)
                otoño.imageAlpha = 255
                verano.imageAlpha = 100
                primavera.imageAlpha = 100
                invierno.imageAlpha = 100
                argazkia.setImageResource(R.drawable.otono)
                myCurrentSeason = Food.Seasons.AUTUMN
            }
        }

        otoño.setOnClickListener{
            titulo.text=getString(R.string.O)
            otoño.imageAlpha = 255
            invierno.imageAlpha = 100
            primavera.imageAlpha = 100
            invierno.imageAlpha = 100
            argazkia.setImageResource(R.drawable.otono)
            myCurrentSeason = Food.Seasons.AUTUMN
            ReadDatabase()
        }
        primavera.setOnClickListener{
            titulo.text=getString(R.string.P)
            primavera.imageAlpha = 255
            invierno.imageAlpha = 100
            verano.imageAlpha = 100
            otoño.imageAlpha = 100
            argazkia.setImageResource(R.drawable.primavera)
            myCurrentSeason = Food.Seasons.SPRING
            ReadDatabase()
        }
        verano.setOnClickListener{
            titulo.text=getString(R.string.V)
            verano.imageAlpha = 255
            invierno.imageAlpha = 100
            primavera.imageAlpha = 100
            otoño.imageAlpha = 100
            argazkia.setImageResource(R.drawable.verano)
            myCurrentSeason = Food.Seasons.SUMMER
            ReadDatabase()
        }
        invierno.setOnClickListener{
            titulo.text=getString(R.string.I)
            invierno.imageAlpha = 255
            primavera.imageAlpha = 100
            verano.imageAlpha = 100
            otoño.imageAlpha = 100
            argazkia.setImageResource(R.drawable.invierno)
            myCurrentSeason = Food.Seasons.WINTER
            ReadDatabase()
        }


        ReadDatabase()

    }
    fun ReadDatabase(){
        comida.clear()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        BD.GetFood(this) { foods ->
            foods.forEach { food ->
                comida.add(food)
            }
            progressBar.visibility = View.GONE
            CargarLista(myCurrentSeason)
        }
    }
    fun CargarLista(season: Food.Seasons){
        val comidaFiltrada = comida.filter { it.season == season }

        val adapter = SeasonFoodAdapter(this, comidaFiltrada)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }
}