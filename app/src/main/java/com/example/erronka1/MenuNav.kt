package com.example.erronka1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuNav : AppCompatActivity() {
    companion object {

        //Declara la que clase redirege cada boton del menu
        val clases = mapOf(
            R.id.inicio to MainActivity::class.java,
            R.id.usuario to Login::class.java,
            R.id.pedidos to MainActivity::class.java,
            R.id.comprar to MainActivity::class.java
        )
        //Guarda clase en la que esta actualmente
        var seleccionado : Int = 0

        fun Crear(context: Context) {

            //Crea el menu
            val navMenu = (context as AppCompatActivity).findViewById<BottomNavigationView>(R.id.navMenu)
            navMenu.inflateMenu(R.menu.nav_menu)

            //Selecciona la clase actual
            navMenu.selectedItemId = seleccionado

            //Acciones de los botones
            navMenu.setOnItemSelectedListener { menuItem ->
                val activityClass = clases[menuItem.itemId]

                if (clases != null) {
                    context.startActivity(Intent(context, activityClass))
                    seleccionado = menuItem.itemId
                    true
                } else false
            }
        }

        //Selecciona a que clase te vas a dirigir
        fun Seleccionar(i : Int){
            val ids = listOf<Int>(2131230963, 2131231240, 2131231083, 2131230854)
            seleccionado = ids[i]
        }
    }
}