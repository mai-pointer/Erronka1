package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
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
    val orders = mutableListOf<Order>()
    val user = FirebaseAuth.getInstance().currentUser

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
        if (user != null){

            //Busca la info de la comida
            var comidaList = mutableListOf<FoodIncomplete>()

            myRefFood.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot){
                    for (orderSnapshot in dataSnapshot.children){
                        //Guarda la informacion en una lista
                        comidaList.add(FoodIncomplete(
                            orderSnapshot.child("food_id").getValue(String::class.java),
                            orderSnapshot.child("food_title").getValue(String::class.java),
                            orderSnapshot.child("food_price").getValue(Double::class.java),
                        ))
                    }
                }
                override fun onCancelled(error: DatabaseError){
                    Log.e("firebase", "Error getting data", error.toException())
                }
            })

            //Crear los pedidos
            myRefOrder.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (orderSnapshot in dataSnapshot.children) {

                        //Variables de order
                        var order: Order
                        val userId = orderSnapshot.child("user_id").getValue(String::class.java)
                        val orderStatus = orderSnapshot.child("order_status").getValue(String::class.java)

                        //Comprueba que los pedidos sean del usuario y que ya los a pedido
                        if(userId == user.uid && orderStatus == "delivered"){

                            val orderId = orderSnapshot.child("order_id").getValue(String::class.java)
                            val orderDateString = orderSnapshot.child("order_date").getValue(String::class.java)
                            val foodId = orderSnapshot.child("food_id").getValue(String::class.java)

                            // Formatea la fecha
                            val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                            var orderDate = Date()
                            try {
                                orderDate = iso8601Format.parse(orderDateString)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            val desiredFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val formattedDate = desiredFormat.format(orderDate)

                            //Crea el objeto dr order
                            order = Order(orderId, formattedDate)

                            //Suma el precio y los nombres de todas las comidas
                            if (foodId != null){
                                var precio: Double = 0.0
                                var lista: String = ""

                                val foodIdList = foodId.split(",").map { it.trim() }

                                for (comida in comidaList){
                                    if (foodIdList.contains(comida.id)) {
                                        lista += " - ${comida.nombre} \n"
                                        precio += comida.precio ?: 0.0
                                    }
                                }

                                order.price = precio
                                order.list = lista
                            }

                            //Añade el objeto a una lista
                            orders.add(order)
                        }
                    }
                    //Llama al creador del ListView
                    Crear()
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", "Error getting data", error.toException())
                }
            })
        }
    }

    fun Crear(){
        //ListView
        val adapter = OrderAdapter(this, orders)
        findViewById<ListView>(R.id.lista_pedidos).adapter = adapter
    }

    //Clases locales
    data class FoodIncomplete(
        val id: String?,
        val nombre: String?,
        val precio: Double?
    )
}