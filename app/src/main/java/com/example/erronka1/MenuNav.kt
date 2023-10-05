package com.example.erronka1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuNav : AppCompatActivity() {
    companion object {

        val menuToActivityMap = mapOf(
            R.id.inicio to MainActivity::class.java,
            R.id.usuario to MainActivity::class.java,
            R.id.pedidos to MainActivity::class.java,
            R.id.comprar to MainActivity::class.java
        )

        var seleccionado : Int = 0

        fun Crear(context: Context) {

            val navMenu = (context as AppCompatActivity).findViewById<BottomNavigationView>(R.id.navMenu)
            navMenu.inflateMenu(R.menu.nav_menu)

            navMenu.selectedItemId = seleccionado
            seleccionado = -1

            navMenu.setOnItemSelectedListener { menuItem ->
                val activityClass = menuToActivityMap[menuItem.itemId]

                if (activityClass != null) {
                    context.startActivity(Intent(context, activityClass))
                    seleccionado = menuItem.itemId
                    true
                } else false
            }
        }
    }
}