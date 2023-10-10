package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class Usuario : AppCompatActivity() {

    //Variables
    lateinit var usuTxt : TextView
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        //Declara las cariables
        usuTxt = findViewById(R.id.usuarioTxt)
        usuTxt.text = mAuth.currentUser?.email.toString()

        //Crea el menu
        MenuNav.Crear(this)

        //Btn de cerrar sesion
        findViewById<Button>(R.id.cerrarBtn).setOnClickListener{
            mAuth.signOut()

            MenuNav.Seleccionar(0)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}