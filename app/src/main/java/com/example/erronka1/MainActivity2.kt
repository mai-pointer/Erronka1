package com.example.erronka1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {

    private lateinit var invierno: ImageView
    private lateinit var otoño: ImageView
    private lateinit var verano: ImageView
    private lateinit var primavera: ImageView
    private lateinit var titulo: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        invierno = findViewById(R.id.imageView4)
        otoño = findViewById(R.id.otoño)
        verano = findViewById(R.id.verano)
        primavera = findViewById(R.id.primavera)
        titulo = findViewById(R.id.textView)
        val nuevoTamañoTexto = 40f
        titulo.textSize = nuevoTamañoTexto

        otoño.setOnClickListener{
            val Intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
        }
        primavera.setOnClickListener{
            val Intent: Intent = Intent(this, MainActivity3::class.java)
            startActivity(Intent)
        }
        verano.setOnClickListener{
            val Intent: Intent = Intent(this, MainActivity4::class.java)
            startActivity(Intent)
        }
    }
}