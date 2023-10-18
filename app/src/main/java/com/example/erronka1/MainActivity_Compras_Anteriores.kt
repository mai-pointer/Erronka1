package com.example.erronka1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class MainActivity_Compras_Anteriores : AppCompatActivity() {

    val orders = listOf<Order>(
//        Order(
//            id = 1,
//            desc = """
//            - Macarrones
//            - Comida inventada 1
//            - Comida inventada 2
//        """.trimIndent(),
//            price = 25.99,
//            data = LocalDate.of(2023, 9, 15)  // Fecha: 15 de septiembre de 2023
//        ),
//        Order(
//            id = 2,
//            desc = """
//            - Comida inventada 3
//            - Comida inventada 4
//            - Comida inventada 5
//        """.trimIndent(),
//            price = 35.50,
//            data = LocalDate.of(2023, 9, 20)  // Fecha: 20 de septiembre de 2023
//        )
    )

    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_compras_anteriores)

        MenuNav.Crear(this, user)


    }
}