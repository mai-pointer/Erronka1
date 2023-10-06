package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainActivity4 : AppCompatActivity() {

    private lateinit var invierno: ImageView
    private lateinit var otoño: ImageView
    private lateinit var verano: ImageView
    private lateinit var primavera: ImageView
    private lateinit var titulo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        invierno = findViewById(R.id.invierno)
        otoño = findViewById(R.id.otoño)
        verano = findViewById(R.id.imageView4)
        primavera = findViewById(R.id.primavera)
        titulo = findViewById(R.id.textView)
        val nuevoTamañoTexto = 40f
        titulo.textSize = nuevoTamañoTexto

        otoño.setOnClickListener{
            val Intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
        }
        invierno.setOnClickListener{
            val Intent: Intent = Intent(this, MainActivity2::class.java)
            startActivity(Intent)
        }
        primavera.setOnClickListener{
            val Intent: Intent = Intent(this, MainActivity3::class.java)
            startActivity(Intent)
        }
    }
}