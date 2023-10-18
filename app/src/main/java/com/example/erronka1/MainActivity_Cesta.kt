package com.example.erronka1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity_Cesta : AppCompatActivity() {

    val comida = listOf<Food>(
    )

    val user = FirebaseAuth.getInstance().currentUser

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cesta)

        MenuNav.Crear(this, user)

        val adapter = FoodAdapter(this, comida, 1)
        findViewById<ListView>(R.id.lista_compras).adapter = adapter

        findViewById<TextView>(R.id.total).text = "${comida.sumOf { it.price!! }}€"
    }
}