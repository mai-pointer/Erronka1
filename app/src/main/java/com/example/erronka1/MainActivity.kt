package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Crea el menu
        MenuNav.Crear(this);

        //Botones para pruebas
        findViewById<Button>(R.id.prueba3Btn).setOnClickListener{
            MenuNav.Seleccionar(0)
            val intent = Intent(this, Proveedores::class.java)
            startActivity(intent)
        }
    }
}