package com.example.erronka1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class PantallaInvierno : AppCompatActivity() {

    private lateinit var invierno: ImageView
    private lateinit var otoño: ImageView
    private lateinit var verano: ImageView
    private lateinit var primavera: ImageView
    private lateinit var titulo: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invierno)

        invierno = findViewById(R.id.imageView4)
        otoño = findViewById(R.id.otoño)
        verano = findViewById(R.id.verano)
        primavera = findViewById(R.id.primavera)
        titulo = findViewById(R.id.textView)
        val nuevoTamañoTexto = 40f

        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        titulo.textSize = nuevoTamañoTexto

        otoño.setOnClickListener{
            val Intent: Intent = Intent(this, PantallaOtoño::class.java)
            startActivity(Intent)
        }
        primavera.setOnClickListener{
            val Intent: Intent = Intent(this, PantallaPrimavera::class.java)
            startActivity(Intent)
        }
        verano.setOnClickListener{
            val Intent: Intent = Intent(this, PantallaVerano::class.java)
            startActivity(Intent)
        }
    }
}