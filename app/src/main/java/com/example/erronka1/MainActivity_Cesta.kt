package com.example.erronka1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity_Cesta : AppCompatActivity() {

    val comida = listOf<Food>(
        Food(
            id = 101,
            title = "Pizza Margarita",
            desc = "Una deliciosa pizza con tomate, mozzarella y albahaca",
            price = 10.99,
            pic = "pizza.jpg",
            category = Food.Category.MAIN,
            season = Food.Seasons.SUMMER
        ),
        Food(
            id = 102,
            title = "Helado de Fresa",
            desc = "Un postre refrescante perfecto para el verano",
            price = 4.99,
            pic = "helado.jpg",
            category = Food.Category.STARTER,
            season = Food.Seasons.SUMMER
        )
    )

    val user = FirebaseAuth.getInstance().currentUser

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cesta)

        MenuNav.Crear(this, user)

        val adapter = FoodAdapter(this, comida, 1)
        findViewById<ListView>(R.id.lista_compras).adapter = adapter

        findViewById<TextView>(R.id.total).text = "${comida.sumOf { it.price }}€"
    }
}