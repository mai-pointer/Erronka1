package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.example.erronka1.R.*

class MainActivity : AppCompatActivity() {
    private lateinit var select: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        MenuNav.Crear(this)
        select = findViewById(id.btnCarta)

        select.setOnClickListener {
            Toast.makeText(this@MainActivity, "EL boton funciona", Toast.LENGTH_SHORT).show()
        }
    }
}