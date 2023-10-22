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

        //Cargar de la base de datos
        /*BD.GetFood {foods->
            ChangeData(foods)
        }*/
        ChangeData(selectedFoodList.selectedFoodList)
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


