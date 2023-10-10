package com.example.erronka1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.erronka1.Proveedor
import com.example.erronka1.Proveedor as Proveedor1


class Proveedores : AppCompatActivity() {

    //Variables
    var colorOriginal: Drawable? = null
    var seleccionado = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proveedores)

        //Crea el menu
        MenuNav.Crear(this)

        //Consige el json
        var proveedores: List<Proveedor> = JSON.Get(this, R.raw.proveedores)

        //Crear el list View
        val listView = findViewById<ListView>(R.id.lista)
        val adapter = ProveedorAdapter(this, proveedores)
        listView.adapter = adapter

        // Agregar un OnItemClickListener al ListView
        listView.setOnItemClickListener { _, _, cont, _ ->

            if (colorOriginal == null){
                colorOriginal = listView.getChildAt(cont).background
                Log.i("Error-TX", colorOriginal.toString() + "----" + listView.getChildAt(cont).background)
            }

            if(seleccionado >= 0) listView.getChildAt(seleccionado).background = colorOriginal
            listView.getChildAt(cont).background = ColorDrawable(getColor(R.color.colorgrey))
            seleccionado = cont

            //Cambia la tarjeta
            findViewById<TextView>(R.id.nombreProvTxt).text = proveedores[cont].nombre
            findViewById<TextView>(R.id.desProvTxt).text = proveedores[cont].descripcion
            findViewById<TextView>(R.id.listaProvTxt).text = " - ${proveedores[cont].productos.joinToString(separator = "\n - ")}"
        }

    }
}


//Adaptador del proveedor

class ProveedorAdapter(private val context: Context, private val proveedores: List<Proveedor1>) : BaseAdapter() {

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

        val proveedor = getItem(position) as Proveedor1

        viewHolder.providerName.text = proveedor.nombre

        return view
    }

    private class ViewHolder(view: View) {
        val providerImage: ImageView = view.findViewById(R.id.provider_image)
        val providerName: TextView = view.findViewById(R.id.provider_name)
    }
}