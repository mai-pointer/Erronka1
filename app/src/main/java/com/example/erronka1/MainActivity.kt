package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.example.erronka1.R.*
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.Date
import android.widget.SearchView;

class MainActivity : AppCompatActivity() {
    private lateinit var menu: LinearLayout
    private lateinit var delivery: LinearLayout
    private lateinit var providers: LinearLayout
    private lateinit var zeroWaste: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        menu = findViewById(id.btnCarta)
        delivery = findViewById(id.btnDelivery)
        providers = findViewById(id.btnProviders)
        zeroWaste = findViewById(id.btnZeroWaste)

        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        menu.setOnClickListener {

            when (obtenerFechaActual()) {

                1, 2, 3 -> {
                    val intent = Intent(this, PantallaInvierno::class.java)
                    intent.putExtra("season", getString(R.string.I))
                    startActivity(intent)
                }

                4, 5, 6 -> {
                    val intent = Intent(this, PantallaInvierno::class.java)
                    intent.putExtra("season", getString(R.string.P))
                    startActivity(intent)
                }

                7, 8, 9 -> {
                    val intent = Intent(this, PantallaInvierno::class.java)
                    intent.putExtra("season", getString(R.string.V))
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(this, PantallaInvierno::class.java)
                    intent.putExtra("season", getString(R.string.O))
                    startActivity(intent)
                }
            }
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

    fun obtenerFechaActual(): Int {
        val calendario = Calendar.getInstance()
        val mes = calendario.get(Calendar.MONTH) + 1
        return mes
    }
}