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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.example.erronka1.Proveedor as Proveedor1


class Proveedores : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap


    //Variables
//    var colorOriginal: Drawable? = null
    var seleccionado = -1
    lateinit var proveedores: List<Proveedor>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proveedores)

        createMap()

        //Crea el menu
        val user = FirebaseAuth.getInstance().currentUser
        MenuNav.Crear(this, user)

        //Consige el json
        proveedores = JSON.Get(this, R.raw.proveedores)

        //Crear el list View
        val listView = findViewById<ListView>(R.id.lista)
        val adapter = ProveedorAdapter(this, proveedores)
        listView.adapter = adapter

        // Agregar un OnItemClickListener al ListView
        listView.setOnItemClickListener { _, _, cont, _ ->

            seleccionado = cont

            //Cambia la tarjeta
            findViewById<TextView>(R.id.nombreProvTxt).text = proveedores[cont].nombre
            findViewById<TextView>(R.id.desProvTxt).text = proveedores[cont].descripcion
            findViewById<TextView>(R.id.listaProvTxt).text = " - ${proveedores[cont].productos.joinToString(separator = "\n - ")}"
        }

    }

    //Generamos el mapa en el fragmento
    private fun createMap(){
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //Una vez el mapa esta generado, asignamos el mapa y llamamos a 2 funciones
    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap
        createMarkerProvedor()
        zoomMapa()
    }


    //Generamos las marcas pasando por los datos de toda la lista
    private fun createMarkerProvedor() {
        proveedores.forEach { i ->
            val coordinates = LatLng(i.latitud, i.longitud)
            val marker = MarkerOptions().position(coordinates).title(i.nombre)
            map.addMarker(marker)

            //Añadimos los ClickListener para cargar los datos de los provedores
            map.setOnMarkerClickListener { clickedMarker ->
                val proveedor = proveedores.find { it.nombre == clickedMarker.title }

                if (proveedor != null) {
                    findViewById<TextView>(R.id.nombreProvTxt).text = proveedor.nombre
                    findViewById<TextView>(R.id.desProvTxt).text = proveedor.descripcion
                    findViewById<TextView>(R.id.listaProvTxt).text = " - ${proveedor.productos.joinToString(separator = "\n - ")}"
                }
                false
            }
            //Añadimos una animacion para mover la camara
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(coordinates, 16f),
                1000,
                null
            )
        }
    }

    //Hacemos un zoom al mapa en Vizkaya
    private fun zoomMapa(){
        val coordinates = LatLng(43.26271 ,-2.92528 )
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,11f),
            1000,
            null
        )
    }


}
