package com.example.erronka1

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity_Pagina_Tienda : AppCompatActivity() {

    ///ELIMINAR******************************************
    // Objetos GSorpresa
    val sorpresa1 = GSorpresa(
        id = 1,
        title = "Regalo Sorpresa 1",
        desc = "Un regalo sorpresa emocionante",
        price = 9.99,
        pic = "sorpresa1.jpg"
    )

    val sorpresa2 = GSorpresa(
        id = 2,
        title = "Caja Misteriosa",
        desc = "¡Nunca sabrás lo que hay dentro!",
        price = 14.99,
        pic = "misterio.jpg"
    )

    // Objetos Food
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
    ///ELIMINAR******************************************

    val user = FirebaseAuth.getInstance().currentUser
    val botones = listOf(
        BotonesInfo(R.id.primeros_platos, Food.Category.STARTER),
        BotonesInfo(R.id.entrantes_platos, Food.Category.STARTER)
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pagina_tienda)

        MenuNav.Crear(this, user)

        for (boton in botones){
            findViewById<Button>(boton.boton).setOnClickListener{
                cargarList(boton.categoria)
            }
        }
    }

    fun cargarList(categoria : Food.Category){
        val comidaFiltrada = comida.filter { it.category == categoria }

        val adapter = FoodAdapter(this, comidaFiltrada)
        findViewById<ListView>(R.id.lista_platos).adapter = adapter
    }
}

class FoodAdapter(private val context: Context, private val foodList: List<Food>) : BaseAdapter() {
    //Funciones del adaptador
    override fun getCount(): Int {
        return foodList.size
    }
    override fun getItem(position: Int): Any {
        return foodList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //Edita la view
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        //Declara la view y el alamcen de los datos
        val view: View
        val elementos: Elementos

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.platos_tienda, parent, false)
            elementos = Elementos() // declara la clase

            // Obtener las vistas del diseño
            elementos.textTitle = view.findViewById(R.id.titulo_platos)
            elementos.textDescription = view.findViewById(R.id.descripcion_platos)
            elementos.textPrice = view.findViewById(R.id.precio_platos)
            elementos.imageFood = view.findViewById(R.id.imagen_platos)
            elementos.buttonAdd = view.findViewById(R.id.boton_platos)

            view.tag = elementos
        } else {
            view = convertView
            elementos = view.tag as Elementos
        }

        // Obtener el objeto Food en esta posición
        val food = foodList[position]

        // Establecer los valores en las vistas
        elementos.textTitle.text = food.title
        elementos.textDescription.text = food.desc
        elementos.textPrice.text = "${context.getString(R.string.precio)}: ${food.price} €"
        // *** Aquí debes cargar la imagen en holder.imageFood ***
        elementos.buttonAdd.setOnClickListener {
            // *** Agregar lógica para manejar el botón "Añadir" ***
        }

        return view
    }

    //Clase para guardar los elementos de la view
    private class Elementos {
        lateinit var textTitle: TextView
        lateinit var textDescription: TextView
        lateinit var textPrice: TextView
        lateinit var imageFood: ImageView
        lateinit var buttonAdd: Button
    }
}

data class BotonesInfo(
    var boton: Int,
    var categoria: Food.Category
)