package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Proveedores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proveedores)

        MenuNav.Crear(this)

    }
}