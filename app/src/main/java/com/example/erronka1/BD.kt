package com.example.erronka1

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BD {
    //OBJETOS INDIVIDUALES PARA COGER DATOS DE CADA BD
    companion object {
        //Comidas
        fun GetFood(callback: (list: List<Food>) -> Unit) {
           val bd = Create("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db"){Snapshot ->
               Food(
                   Snapshot.child("food_id").getValue(String::class.java),
                   Snapshot.child("food_title").getValue(String::class.java),
                   Snapshot.child("food_desc").getValue(String::class.java),
                   Snapshot.child("food_price").getValue(Double::class.java),
                   Snapshot.child("food_pic").getValue(String::class.java),
                   Snapshot.child("food_selected").getValue(Boolean::class.java),
                   Food.Category.from(Snapshot.child("food_category").getValue(String::class.java) ?: ""),
                   Food.Seasons.from(Snapshot.child("food_season").getValue(String::class.java) ?: "")
               )
           }
            bd.Get (callback)
        }
        //Carro de la compra
        fun GetCart(callback: (list: List<Cart>) -> Unit) {
            val bd = Create("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/shopping_cart"){Snapshot ->
                Cart(
                    Snapshot.child("cart_id").getValue(String::class.java),
                    Snapshot.child("food_id").getValue(String::class.java),
                    Snapshot.child("user_id").getValue(String::class.java)
                    )
            }
            bd.Get (callback)
        }
        //Carro de la compra
        fun GetGose(callback: (list: List<GSorpresa>) -> Unit) {
            val bd = Create("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/goseS_db"){Snapshot ->
                GSorpresa(
                    Snapshot.child("goseS_id").getValue(String::class.java),
                    Snapshot.child("goseS_title").getValue(String::class.java),
                    Snapshot.child("goseS_desc").getValue(String::class.java),
                    Snapshot.child("goseS_price").getValue(Double::class.java),
                    Snapshot.child("goseS_pic").getValue(String::class.java),
                )
            }
            bd.Get (callback)
        }
        //Añadir nuevos datos
        fun <T> Añadir(bd: String, producto: T){
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/"+bd.trim())

            myRef.push().setValue(producto)
        }

    }




    //OBJETO GLOBAL PARA CREAR LLAMADAS A LA BD
    class Create<T>(
        var referencia: String,
        val funcion: (snapshot: DataSnapshot)  -> T
    ){

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

