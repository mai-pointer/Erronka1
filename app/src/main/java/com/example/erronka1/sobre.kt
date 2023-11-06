package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class sobre : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sobre)

        //Crea el menu
        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)
    }
}