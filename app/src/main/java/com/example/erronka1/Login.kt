package com.gernika.bd1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var emailTxt: EditText
    lateinit var passTxt: EditText

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailTxt = findViewById(R.id.emailTxt)
        passTxt = findViewById(R.id.passTxt)

        findViewById<Button>(R.id.registrarBtn).setOnClickListener{
            if (!vacio()){

                mAuth.createUserWithEmailAndPassword(emailTxt.text.toString(), passTxt.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Acceder()
                        } else {
                            //Error("Ese usuario ya existe")
                            Error("${task.exception?.message}")
                        }
                    }
            }
        }

        findViewById<Button>(R.id.accederBtn).setOnClickListener{
            if (!vacio()) {

                mAuth.signInWithEmailAndPassword(emailTxt.text.toString(), passTxt.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Acceder()
                        } else {
                            //Error("El email o la contraseña no son correctas")
                            Error("${task.exception?.message}")
                        }
                    }

            }
        }
    }

    fun Acceder(){
        val intent = Intent(this, Inicio::class.java)
        //intent.putExtra(getString(R.string.email), emailTxt.text.toString())
        intent.putExtra(getString(R.string.pass), passTxt.text.toString())

        startActivity(intent)
    }

    fun vacio(): Boolean{
        var vacio = emailTxt.text.toString().trim().isEmpty()
        vacio = emailTxt.text.toString().trim().isEmpty() || vacio

        if (vacio){
            Error(getString(R.string.error3))
        }

        return vacio
    }

    fun Error(texto: String){
        Toast.makeText(this, getString(R.string.Error) + ": " +texto, Toast.LENGTH_LONG).show()
    }
}