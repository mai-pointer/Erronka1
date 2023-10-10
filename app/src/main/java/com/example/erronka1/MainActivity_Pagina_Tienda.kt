package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity_Pagina_Tienda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pagina_tienda)

        MenuNav.Crear(this)
    }
}