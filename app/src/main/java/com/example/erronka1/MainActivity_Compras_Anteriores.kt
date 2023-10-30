package com.example.erronka1

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firestore.v1.StructuredQuery
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity_Compras_Anteriores : AppCompatActivity() {

    //Variables
    val user = FirebaseAuth.getInstance().currentUser
    val myId = user?.uid

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_compras_anteriores)

        //MenuNav
        MenuNav.Crear(this, user)

        
        //BD
        if (user != null) {

            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.VISIBLE

            //Llama a la BD para conseguir la ordenes
            BD.GetOrders { allOrders ->
                val myorders = mutableListOf<Order>()
                //Las filtra
                allOrders?.forEach() { order ->
                    if (order.user_id.equals(myId)) {
                        myorders.add(order)
                    }
                }

                progressBar.visibility = View.GONE

                val neworders = myorders.distinctBy { it.order_id }
                //Crea el adaptador
                if(neworders.isNotEmpty()){
                    Crear(neworders)
                }
            }


        }
    }

    fun Crear(myorders: List<Order>){
        //ListView
        val adapter = OrderAdapter(this, myorders){
            recreate()
        }
        findViewById<ListView>(R.id.lista_pedidos).adapter = adapter
    }
}