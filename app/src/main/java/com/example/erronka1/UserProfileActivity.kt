package com.example.erronka1

import SP
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

class UserProfileActivity : AppCompatActivity() {
    private lateinit var mail: EditText
    private lateinit var changePassword: Button
    private lateinit var logout: Button
    private lateinit var espanol: ImageButton
    private lateinit var euskera: ImageButton
    private lateinit var ingles: ImageButton
    private lateinit var changeMail: ImageButton
    private lateinit var oldPassword: EditText
    private lateinit var newPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //Tema
        val switch = findViewById<Switch>(R.id.tema);
        switch.isChecked = SP.CargarBool(this, "tema")

        switch.findViewById<Switch>(R.id.tema).setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // Cambiar al tema oscuro
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                delegate.applyDayNight()
//                SP.GuardarBool(this, "tema", true)
//            } else {
//                // Cambiar al tema claro (por defecto)
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                delegate.applyDayNight()
//                SP.GuardarBool(this, "tema", false)
//            }
        }

        mail = findViewById(R.id.txtMail)
        changePassword = findViewById(R.id.btnChangePassword)
        changeMail = findViewById(R.id.btnEditMail)
        logout = findViewById(R.id.btnLogout)
        espanol = findViewById(R.id.imageButton)
        euskera = findViewById(R.id.imageButton2)
        ingles = findViewById(R.id.imageButton3)
        oldPassword = findViewById(R.id.oldPassword)
        newPassword = findViewById(R.id.newPassword)




        val passwordChanged = Toast.makeText(this, "Pasahitza aldatu da", Toast.LENGTH_SHORT)
        passwordChanged.setGravity(Gravity.LEFT, 200, 200)

        val passwordNotChanged =
            Toast.makeText(this, "Pasahitza ezin izan da aldatu", Toast.LENGTH_SHORT)
        passwordNotChanged.setGravity(Gravity.LEFT, 200, 200)

        val passwordWrong = Toast.makeText(this, "Pasahitza ez da egokia", Toast.LENGTH_SHORT)
        passwordWrong.setGravity(Gravity.LEFT, 200, 200)

        val auth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = auth.currentUser

        MenuNav.Crear(this, user)

        UpdateData(user)
        mail.isEnabled = false

        changePassword.setOnClickListener() {
            if (user != null) {
                val emailPrev = user.email
                val passwordPrev = oldPassword.text.toString()

                val credential = EmailAuthProvider.getCredential(emailPrev!!, passwordPrev)
                user.reauthenticate(credential)
                    .addOnCompleteListener { reauthTask ->
                        if (reauthTask.isSuccessful) {
                            val newPasswordText = newPassword.text.toString()
                            user.updatePassword(newPasswordText)
                                .addOnCompleteListener { passwordUpdateTask ->
                                    if (passwordUpdateTask.isSuccessful) {
                                        // Contraseña cambiada con éxito
                                        passwordChanged.show()
                                    } else {
                                        passwordNotChanged.show()
                                    }
                                }
                        } else {
                            passwordWrong.show()
                        }
                    }
            }
            }

            changeMail.setOnClickListener() {
                if (mail.isEnabled) {
                    if (user != null) {
                        user.updateEmail(mail.text.toString())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val emailChanged = Toast.makeText(
                                        this,
                                        "Email cambiado con éxito",
                                        Toast.LENGTH_SHORT
                                    )
                                    emailChanged.setGravity(Gravity.LEFT, 200, 200)
                                    emailChanged.show()

                                    UpdateData(user)
                                    mail.isEnabled = false
                                } else {
                                    val emailNotChanged = Toast.makeText(
                                        this,
                                        "No se pudo cambiar el email",
                                        Toast.LENGTH_SHORT
                                    )
                                    emailNotChanged.setGravity(Gravity.LEFT, 200, 200)
                                    emailNotChanged.show()
                                }
                            }
                    }
                } else {
                    mail.isEnabled = true
                }
            }

            logout.setOnClickListener() {
                if (user != null) {
                    auth.signOut()

                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }
            }

        //Idiomas
        val preferenceManager = PreferenceManager.getInstance(this)

        //Envia el idioma español a la funcion
        espanol.setOnClickListener() {
            setLocale("es", resources)
            preferenceManager.saveString("idioma", "es")
//            SP.GuardarString(this, "idioma", "es")
        }
        //Envia el idioma Ingles a la funcion

        ingles.setOnClickListener() {
            setLocale("ing", resources)
            preferenceManager.saveString("idioma", "ing")
//            SP.GuardarString(this, "idioma", "ing")
        }
        //Envia el idioma Euskera a la funcion
        euskera.setOnClickListener() {
            setLocale("eu", resources)
            preferenceManager.saveString("idioma", "eu")
//            SP.GuardarString(this, "idioma", "eu")
        }




        }

        fun UpdateData(user: FirebaseUser?) {
            if (user != null) {
                mail.setText(user.email)
            } else {

            }
        }

    fun setLocale(language: String, resources: Resources) {
        //Reciben el idioma y que es un resource
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(resources.configuration)
        //Se añade el idioma definido en la configuracion para cambiar de strings.xml
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        //Actualica la pagina con los cambios
        recreate()
    }



    }