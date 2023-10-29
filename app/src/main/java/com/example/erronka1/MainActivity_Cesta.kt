package com.example.erronka1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class MainActivity_Cesta : AppCompatActivity() {
    private lateinit var selectedFoodList: SelectedFood
    val user = FirebaseAuth.getInstance().currentUser

    @RequiresApi(Build.VERSION_CODES.O)
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
            if(user != null){
                var orderNumber: Int?
                BD.GetOrdersNoUpdate { orders ->
                    orderNumber = orders?.count()?.plus(1)

                    val orderId = "order_$orderNumber"
                    var myFoods: String = ""
                    var totalPrice: Double = 0.0

                    selectedFoodList.selectedFoodList.forEach { food ->
                        myFoods += "${food.id},"
                        totalPrice += food.price!!
                    }
                    selectedFoodList.selectedGSorpresa.forEach{gSorpresa ->
                        myFoods += "${gSorpresa.id},"
                        totalPrice += gSorpresa.price!!
                    }

                    /*if (myFoods.isNotEmpty()) {
                        myFoods = myFoods.removeSuffix(",")
                    }*/

                    val newOrder = Order(orderId, LocalDate.now().toString(), myFoods, user.uid, totalPrice,Order.Status.from("ordered"))

                    BD.SetOrder(newOrder)

                    val selectedFoodList = SelectedFood.getInstance()
                    selectedFoodList.clearFoodList()

                    val intent = Intent(this, MainActivity_Compras_Anteriores::class.java)
                    MenuNav.Seleccionar(2)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this, Usuario::class.java)
                startActivity(intent)
            }
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


