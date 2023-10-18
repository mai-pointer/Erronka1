package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDate

class MainActivity_Compras_Anteriores : AppCompatActivity() {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val orders = listOf<Order>(
        Order(
            id = "1",
            desc = """
            - Macarrones
            - Comida inventada 1
            - Comida inventada 2
        """.trimIndent(),
            price = 25.99,
            data = dateFormat.parse("2023-09-15") // Fecha: 15 de septiembre de 2023
        ),
        Order(
            id = "2",
            desc = """
            - Comida inventada 3
            - Comida inventada 4
            - Comida inventada 5
        """.trimIndent(),
            price = 35.50,
            data = dateFormat.parse("2023-09-16")   // Fecha: 20 de septiembre de 2023
        )
    )

    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_compras_anteriores)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/order_db")

        //MenuNav
        MenuNav.Crear(this, user)



        //DB
        if (user != null){
            Log.i("Error-TX", user.uid)
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (orderSnapshot in dataSnapshot.children) {
                        if(orderSnapshot.child("user_id").getValue(String::class.java) == user.uid){
                            val orderId = orderSnapshot.child("order_id").getValue(String::class.java)
                            val orderDate = orderSnapshot.child("order_date").getValue(String::class.java)
                            val foodId = orderSnapshot.child("food_id").getValue(String::class.java)
                            val orderStatus = orderSnapshot.child("order_status").getValue(Double::class.java)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", "Error getting data", error.toException())
                }
            })
        }

        //ListView
        val adapter = OrderAdapter(this, orders)
        findViewById<ListView>(R.id.lista_pedidos).adapter = adapter
    }

    fun Crear(){

    }
    fun Añadir(){

    }
}