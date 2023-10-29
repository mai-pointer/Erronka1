package com.example.erronka1


import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.Settings.Global.getString
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
            layout.orderConfirm = view.findViewById(R.id.btnConfirmOrder)

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
        layout.tituloTxt.text = "${context.getString(R.string.pedido_titulo)}: ${elemento.order_date}"
        val foodList = elemento.food_id?.split(",")

        var foodListDecoded = ""
        foodList?.forEach { food ->
            val resourceId = context.resources.getIdentifier(food, "string", context.packageName)
            val decodedString = if (resourceId != 0) context.getString(resourceId) else ""
            foodListDecoded += "$decodedString\n"
        }

        layout.descripcionTxt.text = foodListDecoded
        layout.precioTxt.text = "${context.getString(R.string.precio)}: ${elemento.order_price} €"

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("1wMAfnTstA0Rhe5cVcRUR3xq2r82GNsXB7CxKSM8LYgM/order_db")

        if (elemento.GetStatus() == "delivered"){
            layout.orderConfirm.text = "${context.getString(R.string.entregado)}"
            layout.orderConfirm.isEnabled = false
        } else{
            layout.orderConfirm.text = "${context.getString(R.string.confirmarEntrega)}"
            layout.orderConfirm.setOnClickListener{
                elemento.ChangeStatus()
                layout.orderConfirm.text = "${context.getString(R.string.entregado)}"
                layout.orderConfirm.isEnabled = false
                val orderRef = myRef.child(elemento.order_id.toString())
                orderRef.child("order_status").setValue("delivered")
            }
        }

        return view
    }

    //Almacena los elementos del Layout de la View
    private class ViewLayot {
        lateinit var tituloTxt: TextView
        lateinit var descripcionTxt: TextView
        lateinit var precioTxt: TextView
        lateinit var orderConfirm: Button
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

class FoodSorpresaAdapter(private val context: Context, private val foodList: List<Food>, private val sorpresaList: List<GSorpresa>,  private val selectedFood: SelectedFood) : BaseAdapter() {
    //Funciones del adaptador
    override fun getCount(): Int {
        return foodList.size + sorpresaList.size
    }
    override fun getItem(position: Int): Any {
        return if (position < foodList.size) {
            foodList[position]
        } else {
            sorpresaList[position - foodList.size]
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < foodList.size) {
            0
        } else {
            1
        }
    }

    //Edita la view
    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewType = getItemViewType(position)
        val view: View

        if (viewType == 0) {

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

            if (position < foodList.size) {
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
                }
            }

        } else {

            val elementos: Gose

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.gsorpresa, parent, false)
                elementos = Gose()

                // Obtener las vistas del diseño
                elementos.textTitle = view.findViewById(R.id.titulo_platos)
                elementos.textPrice = view.findViewById(R.id.precio_platos)
                elementos.buttonAdd = view.findViewById(R.id.boton_platos)

                view.tag = elementos
            } else {
                view = convertView
                elementos = view.tag as Gose
            }

            // Obtener el objeto Food en esta posición
            if (position - foodList.size < sorpresaList.size) {
                val food = sorpresaList[position - foodList.size]

                // Establecer los valores en las vistas
                elementos.textTitle.text = food.id
                elementos.textPrice.text = "${context.getString(R.string.precio)}: ${food.price} €"

                when (selectedFood.checkGSorpresa(food)) {
                    false -> {
                        elementos.buttonAdd.text = context.getString(R.string.añadir)
                        elementos.buttonAdd.setOnClickListener {
                            selectedFood.addGSorpresa(food)
                        }
                    }

                    true -> {
                        elementos.buttonAdd.text = context.getString(R.string.quitar)
                        elementos.buttonAdd.setOnClickListener {
                            selectedFood.deleteGSorpresa(food)
                        }

                    }
                }
                elementos.textTitle.text = food.title
                elementos.textPrice.text = "${context.getString(R.string.precio)}: ${food.price} €"
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
    private class Gose {
        lateinit var textTitle: TextView
        lateinit var textPrice: TextView
        lateinit var buttonAdd: Button
    }
}
class SeasonFoodAdapter(private val context: Context, private val foodList: List<Food>) : BaseAdapter() {
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
            view = LayoutInflater.from(context).inflate(R.layout.platos_season, parent, false)
            elementos = Elementos() // declara la clase

            // Obtener las vistas del diseño
            elementos.textTitle = view.findViewById(R.id.titulo_platos)
            elementos.textPrice = view.findViewById(R.id.precio_platos)
            elementos.imageFood = view.findViewById(R.id.imagen_platos)

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

        return view
    }

    //Clase para guardar los elementos de la view
    private class Elementos {
        lateinit var textTitle: TextView
        lateinit var textPrice: TextView
        lateinit var imageFood: ImageView
    }
}

class GSorpresaAdapter(private val context: Context, private val goseList: List<GSorpresa>,  private val selectedFood: SelectedFood) : BaseAdapter() {
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

        when (selectedFood.checkGSorpresa(food)) {
            false -> {
                elementos.buttonAdd.text = context.getString(R.string.añadir)
                elementos.buttonAdd.setOnClickListener {
                    selectedFood.addGSorpresa(food)
                }
            }

            true -> {
                elementos.buttonAdd.text = context.getString(R.string.quitar)
                elementos.buttonAdd.setOnClickListener {
                    selectedFood.deleteGSorpresa(food)
                }

            }
        }
        elementos.textTitle.text = food.title
        elementos.textPrice.text = "${context.getString(R.string.precio)}: ${food.price} €"


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