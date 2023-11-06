package com.example.erronka1

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.erronka1.R.*
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var menu: LinearLayout
    private lateinit var delivery: LinearLayout
    private lateinit var providers: LinearLayout
    private lateinit var zeroWaste: LinearLayout
    private lateinit var aboutUs: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        //Cambia el idioma
        val preferenceManager = PreferenceManager.getInstance(this)
        val valorRecuperado = preferenceManager.getString("idioma", "eu")
        setLocale(valorRecuperado, resources)

//        setLocale(SP.CargarString(this, "idioma") ?: "eu", resources)

        //Cambiar el tema
//        if (SP.CargarBool(this, "tema")) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }

        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        /*FirebaseApp.initializeApp(this)
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )*/

        menu = findViewById(id.btnCarta)
        delivery = findViewById(id.btnDelivery)
        providers = findViewById(id.btnProviders)
        zeroWaste = findViewById(id.btnErreserbak)
        aboutUs = findViewById(id.btnAboutUs)

        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        menu.setOnClickListener {

            when (obtenerFechaActual()) {

                1, 2, 3 -> {
                    val intent = Intent(this, PantallaInvierno::class.java)
                    val bundle = Bundle()
                    bundle.putString("season", getString(string.I))
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                4, 5, 6 -> {
                    val intent = Intent(this, PantallaInvierno::class.java)
                    val bundle = Bundle()
                    bundle.putString("season", getString(string.P))
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                7, 8, 9 -> {
                    val intent = Intent(this, PantallaInvierno::class.java)
                    val bundle = Bundle()
                    bundle.putString("season", getString(string.V))
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(this, PantallaInvierno::class.java)
                    val bundle = Bundle()
                    bundle.putString("season", getString(string.O))
                    intent.putExtras(bundle)
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
            val intent = Intent(this, ErreserbakActivity::class.java)
            startActivity(intent)
        }
        aboutUs.setOnClickListener{
            val intent = Intent(this, ErreserbakActivity::class.java)
            startActivity(intent)
        }
    }

    fun obtenerFechaActual(): Int {
        val calendario = Calendar.getInstance()
        val mes = calendario.get(Calendar.MONTH) + 1
        return mes
    }

    fun setLocale(language: String, resources: Resources) {
        //Reciben el idioma y que es un resource
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(resources.configuration)
        //Se añade el idioma definido en la configuracion para cambiar de strings.xml
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}