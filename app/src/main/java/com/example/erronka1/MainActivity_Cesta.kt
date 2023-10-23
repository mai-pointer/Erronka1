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
        BD.GetFood {foods->
            //Adaptador
            val adapter = FoodAdapter(this, foods, 1)
            findViewById<ListView>(R.id.lista_compras).adapter = adapter
            //Precio
            findViewById<TextView>(R.id.total).text = "${foods.sumOf { it.price!! }}€"
        }

    }
}