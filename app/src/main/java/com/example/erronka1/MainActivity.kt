package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MenuNav.Crear(this);

        val mAuth = FirebaseAuth.getInstance()

        findViewById<TextView>(R.id.prueba1Txt).text = mAuth.currentUser?.email.toString()

        findViewById<Button>(R.id.prueba1Btn).setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.prueba2Btn).setOnClickListener{
            mAuth.signOut()
            findViewById<TextView>(R.id.prueba1Txt).text = mAuth.currentUser?.email.toString()
        }

        findViewById<Button>(R.id.prueba3Btn).setOnClickListener{
            val intent = Intent(this, Proveedores::class.java)
            startActivity(intent)
        }
    }
}