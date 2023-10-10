package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.example.erronka1.R.*

class MainActivity : AppCompatActivity() {
    private lateinit var menu: LinearLayout
    private lateinit var delivery: LinearLayout
    private lateinit var providers: LinearLayout
    private lateinit var zeroWaste: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        MenuNav.Crear(this)
        menu = findViewById(id.btnCarta)
        delivery = findViewById(id.btnDelivery)
        providers = findViewById(id.btnProviders)
        zeroWaste = findViewById(id.btnZeroWaste)


        menu.setOnClickListener {

        }

        delivery.setOnClickListener {
            val intent = Intent(this, MainActivity_Pagina_Tienda::class.java)
            startActivity(intent)
        }

        providers.setOnClickListener {
            val intent = Intent(this, Proveedores::class.java)
            startActivity(intent)
        }

        zeroWaste.setOnClickListener {
            /*val intent = Intent(this, ZeroWasteActivity::class.java)
            startActivity(intent)*/
        }
    }
}