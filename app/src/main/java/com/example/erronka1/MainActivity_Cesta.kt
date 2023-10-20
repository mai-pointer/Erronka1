package com.example.erronka1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MainActivity_Cesta : AppCompatActivity() {
    val user = FirebaseAuth.getInstance().currentUser

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cesta)

        MenuNav.Crear(this, user)

        //Cargar de la base de datos
        if (user != null){
            BD.GetFood {foods->
                BD.GetCart {carts ->

                    var listFiltrada = mutableListOf<Food>()
                    for (cart in carts){
                        if(cart.user == user.uid){
                            foods.find { it.id == cart.food }?.let {
                                listFiltrada.add(
                                    it
                                )
                            }
                        }
                    }

                    //Adaptador
                    val adapter = FoodAdapter(this, listFiltrada, 1)
                    findViewById<ListView>(R.id.lista_compras).adapter = adapter
                    //Precio
                    findViewById<TextView>(R.id.total).text = "${listFiltrada.sumOf { it.price!! }}€"

                }
            }
        }

    }
}