package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class PantallaVerano : AppCompatActivity() {

    private lateinit var invierno: ImageView
    private lateinit var otoño: ImageView
    private lateinit var verano: ImageView
    private lateinit var primavera: ImageView
    private lateinit var titulo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verano)

        invierno = findViewById(R.id.invierno)
        otoño = findViewById(R.id.otoño)
        verano = findViewById(R.id.imageView4)
        primavera = findViewById(R.id.primavera)
        titulo = findViewById(R.id.textView)
        val nuevoTamañoTexto = 40f
        titulo.textSize = nuevoTamañoTexto

        otoño.setOnClickListener{
            val Intent: Intent = Intent(this, PantallaOtoño::class.java)
            startActivity(Intent)
        }
        invierno.setOnClickListener{
            val Intent: Intent = Intent(this, PantallaInvierno::class.java)
            startActivity(Intent)
        }
        primavera.setOnClickListener{
            val Intent: Intent = Intent(this, PantallaPrimavera::class.java)
            startActivity(Intent)
        }
    }
}