package com.example.erronka1

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class PantallaInvierno : AppCompatActivity() {

    private lateinit var invierno: ImageView
    private lateinit var otoño: ImageView
    private lateinit var verano: ImageView
    private lateinit var primavera: ImageView
    private lateinit var titulo: TextView
    private lateinit var argazkia: ImageView



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


        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        val myseason = intent.getStringExtra("season")
        when (myseason) {
            getString(R.string.I) -> {
                titulo.text=getString(R.string.I)
                invierno.visibility = View.GONE
                argazkia.setImageResource(R.drawable.invierno)
            }
            getString(R.string.P) -> {
                titulo.text=getString(R.string.P)
                primavera.visibility = View.GONE
                argazkia.setImageResource(R.drawable.primavera)
            }
            getString(R.string.V) -> {
                titulo.text=getString(R.string.V)
                verano.visibility = View.GONE
                argazkia.setImageResource(R.drawable.verano)
            }
            getString(R.string.O) -> {
                titulo.text=getString(R.string.O)
                otoño.visibility = View.GONE
                argazkia.setImageResource(R.drawable.otono)
            }
        }

        otoño.setOnClickListener{
            titulo.text=getString(R.string.O)
            otoño.visibility = View.GONE
            primavera.visibility = View.VISIBLE
            invierno.visibility = View.VISIBLE
            verano.visibility = View.VISIBLE
            argazkia.setImageResource(R.drawable.otono)
        }
        primavera.setOnClickListener{
            titulo.text=getString(R.string.P)
            primavera.visibility = View.GONE
            invierno.visibility = View.VISIBLE
            otoño.visibility = View.VISIBLE
            verano.visibility = View.VISIBLE
            argazkia.setImageResource(R.drawable.primavera)

        }
        verano.setOnClickListener{
            titulo.text=getString(R.string.V)
            verano.visibility = View.GONE
            primavera.visibility = View.VISIBLE
            otoño.visibility = View.VISIBLE
            invierno.visibility = View.VISIBLE
            argazkia.setImageResource(R.drawable.verano)

        }
        invierno.setOnClickListener{
            titulo.text=getString(R.string.I)
            invierno.visibility = View.GONE
            primavera.visibility = View.VISIBLE
            otoño.visibility = View.VISIBLE
            verano.visibility = View.VISIBLE
            argazkia.setImageResource(R.drawable.invierno)

        }
    }
}