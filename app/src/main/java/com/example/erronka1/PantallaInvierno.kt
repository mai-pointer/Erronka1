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

        val myseason = intent.getStringExtra("season")
        when (myseason) {
            getString(R.string.I) -> {
                titulo.text=getString(R.string.I)
                invierno.visibility = View.GONE
                argazkia.setImageResource(R.drawable.invierno)
                myCurrentSeason = Food.Seasons.WINTER
            }
            getString(R.string.P) -> {
                titulo.text=getString(R.string.P)
                primavera.visibility = View.GONE
                argazkia.setImageResource(R.drawable.primavera)
                myCurrentSeason = Food.Seasons.SPRING
            }
            getString(R.string.V) -> {
                titulo.text=getString(R.string.V)
                verano.visibility = View.GONE
                argazkia.setImageResource(R.drawable.verano)
                myCurrentSeason = Food.Seasons.SUMMER
            }
            getString(R.string.O) -> {
                titulo.text=getString(R.string.O)
                otoño.visibility = View.GONE
                argazkia.setImageResource(R.drawable.otono)
                myCurrentSeason = Food.Seasons.AUTUMN
            }
        }

        otoño.setOnClickListener{
            titulo.text=getString(R.string.O)
            otoño.visibility = View.GONE
            primavera.visibility = View.VISIBLE
            invierno.visibility = View.VISIBLE
            verano.visibility = View.VISIBLE
            argazkia.setImageResource(R.drawable.otono)
            myCurrentSeason = Food.Seasons.AUTUMN
            comida.clear()
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

                    CargarLista(myCurrentSeason)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", "Error getting data", error.toException())
                }
            })
        }
        primavera.setOnClickListener{
            titulo.text=getString(R.string.P)
            primavera.visibility = View.GONE
            invierno.visibility = View.VISIBLE
            otoño.visibility = View.VISIBLE
            verano.visibility = View.VISIBLE
            argazkia.setImageResource(R.drawable.primavera)
            myCurrentSeason = Food.Seasons.SPRING
            comida.clear()
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

                    CargarLista(myCurrentSeason)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", "Error getting data", error.toException())
                }
            })
        }
        verano.setOnClickListener{
            titulo.text=getString(R.string.V)
            verano.visibility = View.GONE
            primavera.visibility = View.VISIBLE
            otoño.visibility = View.VISIBLE
            invierno.visibility = View.VISIBLE
            argazkia.setImageResource(R.drawable.verano)
            myCurrentSeason = Food.Seasons.SUMMER
            comida.clear()
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

                    CargarLista(myCurrentSeason)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", "Error getting data", error.toException())
                }
            })
        }
        invierno.setOnClickListener{
            titulo.text=getString(R.string.I)
            invierno.visibility = View.GONE
            primavera.visibility = View.VISIBLE
            otoño.visibility = View.VISIBLE
            verano.visibility = View.VISIBLE
            argazkia.setImageResource(R.drawable.invierno)
            myCurrentSeason = Food.Seasons.WINTER
            comida.clear()
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

                    CargarLista(myCurrentSeason)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", "Error getting data", error.toException())
                }
            })
        }


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

                CargarLista(myCurrentSeason)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error getting data", error.toException())
            }
        })

    }
    fun CargarLista(season: Food.Seasons){
        val comidaFiltrada = comida.filter { it.season == season }

        val adapter = SeasonFoodAdapter(this, comidaFiltrada)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }
}