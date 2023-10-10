package com.example.erronka1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Login : AppCompatActivity(), CoroutineScope by MainScope() {

    //Variables
    lateinit var emailTxt: EditText
    lateinit var passTxt: EditText
    lateinit var progressBar: ProgressBar

    val mAuth = FirebaseAuth.getInstance()
    val user:FirebaseUser? = mAuth.currentUser

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Crea el menu
        MenuNav.Crear(this, user);

        //Declara las variables
        emailTxt = findViewById(R.id.emailTxt)
        passTxt = findViewById(R.id.passTxt)
        progressBar = findViewById(R.id.progressBar)

        //Btn registro
        findViewById<Button>(R.id.registrarBtn).setOnClickListener {
            if (!Vacio()) {
                progressBar.visibility = View.VISIBLE
                launch(Dispatchers.IO) {
                    try {
                        mAuth.createUserWithEmailAndPassword(
                            emailTxt.text.toString(),
                            passTxt.text.toString()
                        ).addOnCompleteListener(this@Login) { task ->
                            if (task.isSuccessful) {
                                // Registro exitoso, puedes acceder
                                Acceder()
                            } else {
                                // Error durante el registro
                                val errorMessage = task.exception?.message
                                Error(errorMessage ?: "Error desconocido")
                            }
                        }
                            //cancel()
                            // Ocultar la barra de progreso
                            withContext(Dispatchers.Main) {
                                progressBar.visibility = View.INVISIBLE
                            }

                    } catch (e: Exception) {
                    // Manejar cualquier excepción que ocurra durante la tarea
                    e.printStackTrace()

                    // Asegúrate de ocultar la barra de progreso en caso de error
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.INVISIBLE
                    }
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