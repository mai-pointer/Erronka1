package com.example.erronka1

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity_Compras_Anteriores : AppCompatActivity() {

    //Variables
    val myorders = mutableListOf<Order>()
    val user = FirebaseAuth.getInstance().currentUser
    val myId = user?.providerId

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_compras_anteriores)

        //Referencias a la BD
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRefOrder: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/order_db")
        val myRefFood: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db")

        //MenuNav
        MenuNav.Crear(this, user)
        //BD
        if (user != null) {

            BD.GetOrders { allOrders ->
                allOrders.forEach() { order ->
                    if (order.user_id.equals(myId)) {
                        myorders.add(order)
                    }
                }
                if(myorders.isNotEmpty()){
                    Crear()
                }

            }
        }
    }

    fun Crear(){
        //ListView
        val adapter = OrderAdapter(this, myorders)
        findViewById<ListView>(R.id.lista_pedidos).adapter = adapter
    }

    //Clases locales
    data class FoodIncomplete(
        val id: String?,
        val nombre: String?,
        val precio: Double?
    )
}