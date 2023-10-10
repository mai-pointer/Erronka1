package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class UserProfileActivity : AppCompatActivity() {
    private lateinit var mail: EditText
    //private lateinit var userName: EditText
    private lateinit var changePassword: Button
    private lateinit var changeMail: ImageButton
    //private lateinit var changeUsername: Button
    private lateinit var oldPassword: EditText
    private lateinit var newPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        mail = findViewById(R.id.txtMail)

        changePassword = findViewById(R.id.btnChangePassword)
        changeMail = findViewById(R.id.btnEditMail)

        oldPassword = findViewById(R.id.newPassword)
        newPassword = findViewById(R.id.oldPassword)

        val passwordChanged = Toast.makeText(this, "Pasahitza aldatu da", Toast.LENGTH_SHORT)
        passwordChanged.setGravity(Gravity.LEFT, 200, 200)

        val passwordNotChanged = Toast.makeText(this, "Pasahitza ezin izan da aldatu", Toast.LENGTH_SHORT)
        passwordNotChanged.setGravity(Gravity.LEFT, 200, 200)

        val passwordWrong = Toast.makeText(this, "Pasahitza ez da egokia", Toast.LENGTH_SHORT)
        passwordWrong.setGravity(Gravity.LEFT, 200, 200)

        val auth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = auth.currentUser

        UpdateData(user)

        //userName.isEnabled = false
        mail.isEnabled = false

        changePassword.setOnClickListener(){
            if (user != null) {
                // Crea las credenciales con el email y la contraseña antiguos
                val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword.text.toString())

                // Reautentica al usuario con las credenciales antiguas
                user.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // La reautenticación fue exitosa, puedes cambiar la contraseña ahora
                            user.updatePassword(newPassword.text.toString())
                                .addOnCompleteListener { passwordUpdateTask ->
                                    if (passwordUpdateTask.isSuccessful) {
                                        passwordChanged.show()
                                    } else {
                                        passwordNotChanged.show()
                                    }
                                }
                        } else {
                            passwordWrong.show()
                        }
                    }
            } else {
            }
        }

        changeMail.setOnClickListener(){
            if(mail.isEnabled){
                if (user != null) {
                    user.updateEmail(mail.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val emailChanged = Toast.makeText(this, "Email cambiado con éxito", Toast.LENGTH_SHORT)
                                emailChanged.setGravity(Gravity.LEFT, 200, 200)
                                emailChanged.show()

                                UpdateData(user)
                                mail.isEnabled = false
                            } else {
                                val emailNotChanged = Toast.makeText(this, "No se pudo cambiar el email", Toast.LENGTH_SHORT)
                                emailNotChanged.setGravity(Gravity.LEFT, 200, 200)
                                emailNotChanged.show()
                            }
                        }
                }
            } else{
                mail.isEnabled = true
            }
        }

        /*changeUsername.setOnClickListener(){
            if(userName.isEnabled){
                if (user != null) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(userName.text.toString())
                        .build()

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val usernameChanged = Toast.makeText(this, "Nombre de usuario cambiado con éxito", Toast.LENGTH_SHORT)
                                usernameChanged.setGravity(Gravity.LEFT, 200, 200)
                                usernameChanged.show()
                                UpdateData(user)
                                userName.isEnabled=false

                            } else {
                                val usernameNotChanged = Toast.makeText(this, "No se pudo cambiar el nombre de usuario", Toast.LENGTH_SHORT)
                                usernameNotChanged.setGravity(Gravity.LEFT, 200, 200)
                                usernameNotChanged.show()
                            }
                        }
                }
            }
        }*/
    }

    fun UpdateData(user: FirebaseUser?){
        if (user != null) {
            mail.setText(user.email)
        } else {

        }
    }
}