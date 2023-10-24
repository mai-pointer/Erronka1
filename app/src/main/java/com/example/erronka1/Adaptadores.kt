package com.example.erronka1


import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrderAdapter(private val context: Context, private val lista: List<Order>) : BaseAdapter() {
    //Funciones del adaptador
    override fun getCount(): Int {
        return lista.size
    }
    override fun getItem(position: Int): Any {
        return lista[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //Edita la view
    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        //Declara la view y la clase del layout
        val view: View
        val layout: ViewLayot

        //En caso de que no tenga un layout
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(
                R.layout.pedidos,
                parent,
                false
            ) //Asigna la view del layot creado anteriormente
            layout = ViewLayot() // Le asigna una nueva clase

            // Obtener los elementos del layout
            layout.tituloTxt = view.findViewById(R.id.titulo_pedidos)
            layout.descripcionTxt = view.findViewById(R.id.lista_pedidos)
            layout.precioTxt = view.findViewById(R.id.precio_pedidos)

            //Le asigna el layot a la view
            view.tag = layout
        }
        //En caso de que tenga el layot
        else {
            view = convertView
            layout = view.tag as ViewLayot
        }

        // Obtener el objeto Food en esta posición
        val elemento = lista[position]

        // Establecer los valores en las vistas
        layout.tituloTxt.text = "${context.getString(R.string.pedido_titulo)}: ${elemento.data}"
        layout.descripcionTxt.text = elemento.list
        layout.precioTxt.text = "${context.getString(R.string.precio)}: ${elemento.price} €"

        return view
    }

    //Almacena los elementos del Layout de la View
    private class ViewLayot {
        lateinit var tituloTxt: TextView
        lateinit var descripcionTxt: TextView
        lateinit var precioTxt: TextView
    }
}

class FoodAdapter(private val context: Context, private val foodList: List<Food>,  private val selectedFood: SelectedFood) : BaseAdapter() {
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
    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        //Declara la view y el alamcen de los datos
        val view: View
        val elementos: Elementos

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.platos_tienda, parent, false)
            elementos = Elementos() // declara la clase

            // Obtener las vistas del diseño
            elementos.textTitle = view.findViewById(R.id.titulo_platos)

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
        elementos.textPrice.text = "${context.getString(R.string.precio)}: ${food.price} €"
        food.downloadImageFromCloudStorage { bitmap ->
            if (bitmap != null) {
                elementos.imageFood.setImageBitmap(bitmap)
            } else {
                Toast.makeText(context, "Failed to retrieve image", Toast.LENGTH_SHORT).show()
            }
        }

        when (selectedFood.checkFood(food)) {
            false -> {
                elementos.buttonAdd.text = context.getString(R.string.añadir)
                elementos.buttonAdd.setOnClickListener {
                    selectedFood.addFood(food)
                }
            }
            true -> {
                elementos.buttonAdd.text = context.getString(R.string.quitar)
                elementos.buttonAdd.setOnClickListener {
                    selectedFood.deleteFood(food)
                }

            }
            else -> {}
        }

        fun setItems0(){
            elementos.buttonAdd.text = context.getString(R.string.añadir)
            elementos.buttonAdd.setOnClickListener {
                selectedFood.addFood(food)
            }
        }
        fun setItems1(){
            elementos.buttonAdd.text = context.getString(R.string.quitar)
            elementos.buttonAdd.setOnClickListener {
                selectedFood.addFood(food)
            }
        }

        return view
    }

    //Clase para guardar los elementos de la view
    private class Elementos {
        lateinit var textTitle: TextView
        lateinit var textPrice: TextView
        lateinit var imageFood: ImageView
        lateinit var buttonAdd: Button
    }
}

class GSorpresaAdapter(private val context: Context, private val goseList: List<GSorpresa>) : BaseAdapter() {
    //Funciones del adaptador
    override fun getCount(): Int {
        return goseList.size
    }
    override fun getItem(position: Int): Any {
        return goseList[position]
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
            view = LayoutInflater.from(context).inflate(R.layout.gsorpresa, parent, false)
            elementos = Elementos() // declara la clase

            // Obtener las vistas del diseño
            elementos.textTitle = view.findViewById(R.id.titulo_platos)
            elementos.textPrice = view.findViewById(R.id.precio_platos)
            elementos.buttonAdd = view.findViewById(R.id.boton_platos)

            view.tag = elementos
        } else {
            view = convertView
            elementos = view.tag as Elementos
        }

        // Obtener el objeto Food en esta posición
        val food = goseList[position]

        // Establecer los valores en las vistas
        elementos.textTitle.text = food.title
        elementos.textPrice.text = "${context.getString(R.string.precio)}: ${food.price} €"
        elementos.buttonAdd.setOnClickListener {
            // *** Agregar lógica para manejar el botón "Añadir" ***
        }

        return view
    }

    //Clase para guardar los elementos de la view
    private class Elementos {
        lateinit var textTitle: TextView
        lateinit var textPrice: TextView
        lateinit var buttonAdd: Button
    }
}

class ProveedorAdapter(private val context: Context, private val proveedores: List<Proveedor>) : BaseAdapter() {

    override fun getCount(): Int = proveedores.size
    override fun getItem(position: Int): Any = proveedores[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val proveedor = getItem(position) as Proveedor

        viewHolder.providerName.text = proveedor.nombre

        return view
    }

    private class ViewHolder(view: View) {
        val providerName: TextView = view.findViewById(R.id.provider_name)
    }
}