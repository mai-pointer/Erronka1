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
        fun GetFood(callback: (foods: List<Food>) -> Unit) {
           val bd = Create("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db"){foodSnapshot ->
               Food(
                   foodSnapshot.child("food_id").getValue(String::class.java),
                   foodSnapshot.child("food_title").getValue(String::class.java),
                   foodSnapshot.child("food_desc").getValue(String::class.java),
                   foodSnapshot.child("food_price").getValue(Double::class.java),
                   foodSnapshot.child("food_pic").getValue(String::class.java),
                   foodSnapshot.child("food_selected").getValue(Boolean::class.java),
                   Food.Category.from(foodSnapshot.child("food_category").getValue(String::class.java) ?: ""),
                   Food.Seasons.from(foodSnapshot.child("food_season").getValue(String::class.java) ?: "")
               )
           }
            bd.Get (callback)
        }
        //Carro de la compra
        fun GetCart(callback: (foods: List<Cart>) -> Unit) {
            val bd = Create("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/shopping_cart"){foodSnapshot ->
                Cart(
                    foodSnapshot.child("cart_id").getValue(String::class.java),
                    foodSnapshot.child("food_id").getValue(String::class.java),
                    foodSnapshot.child("user_id").getValue(String::class.java)
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

