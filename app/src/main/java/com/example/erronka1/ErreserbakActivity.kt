package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ErreserbakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erreserbak)
        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        val spinnerordua: Spinner = findViewById(R.id.spinner)
        val spinnerpertsonak: Spinner = findViewById(R.id.spinner2)

        val orduak = listOf("20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00")
        val pertsonak = listOf("1", "2", "3", "4", "5", "6", "7", "8")

        val adapterOrduak = ArrayAdapter(this, android.R.layout.simple_spinner_item, orduak)
        adapterOrduak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterPertsonak = ArrayAdapter(this, android.R.layout.simple_spinner_item, pertsonak)
        adapterPertsonak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerordua.adapter = adapterOrduak
        spinnerpertsonak.adapter = adapterPertsonak

        findViewById<Button>(R.id.confirmar).setOnClickListener{
            GlobalScope.launch {
                delay(2000) // Espera 2 segundos (2000 milisegundos)
                runOnUiThread {
                    Toast.makeText(applicationContext,  getString(R.string.erreserba), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}