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
    private lateinit var selectedFoodList: SelectedFood
    val user = FirebaseAuth.getInstance().currentUser

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cesta)

        MenuNav.Crear(this, user)

        selectedFoodList = SelectedFood.getInstance()
        if (selectedFoodList.eventHandlers == null) {
            selectedFoodList.eventHandlers = mutableListOf()
        }
        selectedFoodList.eventHandlers?.add {list ->
            ChangeData(list)
        }
        ChangeData(selectedFoodList.selectedFoodList)

        findViewById<Button>(R.id.btnPay).setOnClickListener{
            val orderNumber = BD.GetOrders{orders -> orders.count()}
            val orderid = "order_" + orderNumber+1
            val myFoods:MutableList<String>? = null
            selectedFoodList.selectedFoodList.forEach{food ->
                myFoods?.add(food.id.toString())
            }
            val newOrder: Order(orderid, LocalDate.now(), myFoods, user, )

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        selectedFoodList = SelectedFood.getInstance()
        selectedFoodList.eventHandlers?.remove{list ->
            ChangeData(list)
        }
    }
    fun ChangeData (newList: MutableList<Food>) {
        val adapter = FoodAdapter(this, newList, selectedFoodList)
        findViewById<ListView>(R.id.lista_compras).adapter = adapter
        //Precio
        findViewById<TextView>(R.id.total).text = "${newList.sumOf { it.price!! }}€"
    }

}


