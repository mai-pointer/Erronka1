package com.example.erronka1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MainActivity_Cesta : AppCompatActivity() {
    val comida = mutableListOf<Food>()
    val user = FirebaseAuth.getInstance().currentUser

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cesta)

        MenuNav.Crear(this, user)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db")

        // Crear una referencia al Cloud Storage utilizando la instancia de Firebase
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        //Cargar de la base de datos
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (foodSnapshot in dataSnapshot.children) {
                    if ((foodSnapshot.child("food_selected").getValue(Boolean::class.java)) == true)
                    {
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
                }
                CargarLista()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error getting data", error.toException())
            }
        })

    }
    fun CargarLista(){
        val adapter = FoodAdapter(this, comida, 1)
        findViewById<ListView>(R.id.lista_compras).adapter = adapter

        findViewById<TextView>(R.id.total).text = "${comida.sumOf { it.price!! }}€"
    }
}