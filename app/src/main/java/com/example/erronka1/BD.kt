package com.example.erronka1

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BD {
    //OBJETOS INDIVIDUALES PARA COGER DATOS DE CADA BD
    companion object {
        //Comidas
        fun GetFood(callback: (foods: List<Food>) -> Unit) {
            val bd = Create("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db") { foodSnapshot ->
                    Food(
                        foodSnapshot.child("food_id").getValue(String::class.java),
                        context.getString(context.resources.getIdentifier((foodSnapshot.child("food_id").getValue(String::class.java)), "string", context.packageName)),
                        foodSnapshot.child("food_price").getValue(Double::class.java),
                        foodSnapshot.child("food_pic").getValue(String::class.java),
                        Food.Category.from(
                            foodSnapshot.child("food_category").getValue(String::class.java) ?: ""
                        ),
                        Food.Seasons.from(
                            foodSnapshot.child("food_season").getValue(String::class.java) ?: ""
                        )
                    )
                }
            bd.Get(callback)
        }

        //Carro de la compra
        fun GetGose(context: Context, callback: (list: List<GSorpresa>) -> Unit) {
            val bd = Create("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/goseS_db") { Snapshot ->
                GSorpresa(
                    Snapshot.child("goseS_id").getValue(String::class.java),
                    context.getString(context.resources.getIdentifier((Snapshot.child("goseS_id").getValue(String::class.java)), "string", context.packageName)),
                    Snapshot.child("goseS_price").getValue(Double::class.java)
                )
            }
            bd.Get(callback)
        }
        fun GetOrdersNoUpdate(callback: (orders: MutableList<Order>?) -> Unit) {
            val myRef = FirebaseDatabase.getInstance().getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/order_db")
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val orders: MutableList<Order> = mutableListOf() // Inicializa la lista aquí
                    for (orderSnapshot in dataSnapshot.children) {
                        val myOrder = Order(
                            orderSnapshot.child("order_id").getValue(String::class.java),
                            orderSnapshot.child("order_date").getValue(String::class.java),
                            orderSnapshot.child("food_id").getValue(String::class.java),
                            orderSnapshot.child("user_id").getValue(String::class.java),
                            orderSnapshot.child("order_price").getValue(Double::class.java),
                            Order.Status.from(
                                orderSnapshot.child("order_status").getValue(String::class.java) ?: ""
                            )
                        )
                        orders.add(myOrder) // Agrega el pedido a la lista
                    }

                    callback(orders) // Llama al callback con la lista de pedidos
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Maneja cualquier error que ocurra durante la operación
                    Log.e(TAG, "Error al obtener datos", databaseError.toException())
                    callback(null) // Llama al callback con null en caso de error
                }
            })
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun GetOrders(callback: (orders: List<Order>) -> Unit) {
            val bd = Create("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/order_db") { orderSnapshot ->
                    Order(
                        orderSnapshot.child("order_id").getValue(String::class.java),
                        orderSnapshot.child("order_date").getValue(String::class.java),
                        orderSnapshot.child("food_id").getValue(String::class.java),
                        orderSnapshot.child("user_id").getValue(String::class.java),
                        orderSnapshot.child("order_price").getValue(Double::class.java),
                        Order.Status.from(
                            orderSnapshot.child("order_status").getValue(String::class.java) ?: ""
                        )
                    )

                }
            bd.Get(callback)
        }

        fun SetOrder(newOrder: Order) {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference =
                database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/order_db")

            val key = newOrder.order_id
            if (key != null) {
                myRef.child(key).setValue(newOrder).addOnCompleteListener { task ->
                    if(!task.isSuccessful) {
                        Log.e("firebase", "Error writing data")
                    }
                }
            }
        }

        //Añadir nuevos datos
        fun <T> Añadir(bd: String, producto: T, callback: (isSuccess: Boolean) -> Unit) {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/" + bd.trim())
            myRef.setValue(producto).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    Log.e("firebase", "Error writing data")
                    callback(false)
                }
            }

        }

        //OBJETO GLOBAL PARA CREAR LLAMADAS A LA BD
        class Create<T>(
            var referencia: String,
            val funcion: (snapshot: DataSnapshot) -> T
        ) {

            private var list: MutableList<T> = mutableListOf()

            fun Get(callback: (list: List<T>) -> Unit) {
                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                val myRef: DatabaseReference = database.getReference(referencia)

                myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            list.add(
                                funcion(snapshot)
                            )
                        }
                        callback(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("firebase", "Error getting data", error.toException())
                    }
                })
            }
        }
    }


}

