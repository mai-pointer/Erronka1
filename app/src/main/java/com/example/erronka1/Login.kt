package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        MenuNav.Crear(this);

        emailTxt = findViewById(R.id.emailTxt)
        passTxt = findViewById(R.id.passTxt)

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

    fun Acceder(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun Vacio(): Boolean{
        var vacio = emailTxt.text.toString().trim().isEmpty()
        vacio = emailTxt.text.toString().trim().isEmpty() || vacio

        if (vacio){
            Error(getString(R.string.error1))
        }

        return vacio
    }

    fun Error(texto: String){
        Toast.makeText(this, getString(R.string.Tag) + ": " +texto, Toast.LENGTH_LONG).show()
    }
}