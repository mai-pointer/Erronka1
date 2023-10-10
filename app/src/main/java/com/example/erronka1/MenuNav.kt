package com.example.erronka1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MenuNav : AppCompatActivity() {
    companion object {
        val mAuth = FirebaseAuth.getInstance()

        val clases2 = mapOf(
            R.id.inicio to MainActivity::class.java,
            R.id.usuario to UserProfileActivity::class.java,
            R.id.pedidos to MainActivity::class.java,
            R.id.comprar to MainActivity::class.java
        )

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

            if(mAuth != null){
                navMenu.setOnItemSelectedListener { menuItem ->
                    val activityClass = clases2[menuItem.itemId]
                    if (clases2 != null) {
                        context.startActivity(Intent(context, activityClass))
                        seleccionado = menuItem.itemId
                        true
                    } else false
                }
            } else {
                navMenu.setOnItemSelectedListener { menuItem ->
                    val activityClass = clases[menuItem.itemId]
                    if (clases != null) {
                        context.startActivity(Intent(context, activityClass))
                        seleccionado = menuItem.itemId
                        true
                    } else false
                }
            }



        }

        //Selecciona a que clase te vas a dirigir
        fun Seleccionar(i : Int){
            val ids = listOf<Int>(2131230963, 2131231240, 2131231083, 2131230854)
            seleccionado = ids[i]
        }
    }
}