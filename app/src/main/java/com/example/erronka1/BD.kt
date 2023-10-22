package com.example.erronka1

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BD {
    companion object {
        fun GetFood(callback: (foods: MutableList<Food>) -> Unit) {
            val foods = mutableListOf<Food>()
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db")

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (foodSnapshot in dataSnapshot.children) {
                        foods.add(
                            Food(
                                foodSnapshot.child("food_id").getValue(String::class.java),
                                foodSnapshot.child("food_title").getValue(String::class.java),
                                foodSnapshot.child("food_desc").getValue(String::class.java),
                                foodSnapshot.child("food_price").getValue(Double::class.java),
                                foodSnapshot.child("food_pic").getValue(String::class.java),
                                Food.Category.from(foodSnapshot.child("food_category").getValue(String::class.java) ?: ""),
                                Food.Seasons.from(foodSnapshot.child("food_season").getValue(String::class.java) ?: "")
                            )
                        )
                    }
                    callback(foods)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(foods)
                    Log.e("firebase", "Error getting data", error.toException())
                }
            })
            callback(foods)
        }

        fun GetSelectedFood(selectedFood: SelectedFood, callback: (foods: List<Food>) -> Unit) {
            val foods = mutableListOf<Food>()
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/food_db")

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (foodSnapshot in dataSnapshot.children) {
                        val foodId = foodSnapshot.child("food_id").getValue(String::class.java)
                        val foodCategory = foodSnapshot.child("food_category").getValue(String::class.java)
                        val foodDesc = foodSnapshot.child("food_desc").getValue(String::class.java)
                        val foodPic = foodSnapshot.child("food_pic").getValue(String::class.java)
                        val foodPrice = foodSnapshot.child("food_price").getValue(Double::class.java)
                        val foodSeason = foodSnapshot.child("food_season").getValue(String::class.java)
                        val foodTitle = foodSnapshot.child("food_title").getValue(String::class.java)

                        val food = Food(foodId, foodTitle, foodDesc, foodPrice, foodPic, Food.Category.from(foodCategory.toString()), Food.Seasons.from(foodSeason.toString()))

                        if (selectedFood.checkFood(food)){
                            foods.add(food)
                        }
                    }
                    callback(foods)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(foods)
                    Log.e("firebase", "Error getting data", error.toException())
                }
            })
            callback(foods)
        }
    }
}