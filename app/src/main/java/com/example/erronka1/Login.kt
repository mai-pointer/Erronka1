package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    //Variables
    lateinit var emailTxt: EditText
    lateinit var passTxt: EditText

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Redirige a la pantalla de usuario
        if( mAuth.currentUser != null){
            MenuNav.Seleccionar(1)
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        //Crea el menu
        MenuNav.Crear(this);

        //Declara las variables
        emailTxt = findViewById(R.id.emailTxt)
        passTxt = findViewById(R.id.passTxt)

        //Btn registro
        findViewById<Button>(R.id.registrarBtn).setOnClickListener{
            if (!Vacio()){
                mAuth.createUserWithEmailAndPassword(emailTxt.text.toString(), passTxt.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Acceder()
                        } else {
                            Error("${task.exception?.message}")
                        }
                    }
            }
        }

        //Btn acceder
        findViewById<Button>(R.id.accederBtn).setOnClickListener{
            if (!Vacio()) {
                mAuth.signInWithEmailAndPassword(emailTxt.text.toString(), passTxt.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Acceder()
                        } else {
                            Error("${task.exception?.message}")
                        }
                    }

            }
        }
    }

    //Cuando accede redirige a la pantalla principal
    fun Acceder(){
        MenuNav.Seleccionar(0)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //Comprueba si algun campo esta vacio
    fun Vacio(): Boolean{
        val vacio = passTxt.text.toString().trim().isEmpty() || emailTxt.text.toString().trim().isEmpty()
        if (vacio)Error(getString(R.string.error1))

        return vacio
    }

    //Manda los errores
    fun Error(texto: String){
        Toast.makeText(this, getString(R.string.Tag) + ": " +texto, Toast.LENGTH_LONG).show()
    }
}