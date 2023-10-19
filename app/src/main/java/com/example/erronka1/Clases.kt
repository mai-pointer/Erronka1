package com.example.erronka1

import java.time.LocalDate

data class Proveedor(
    var nombre: String,
    var descripcion: String,
    var productos: List<String>,
    var latitud: Double,
    var longitud: Double

)

interface Product {
    val id: String?
    val title: String?
    val desc: String?
    val price: Double?
    val pic: String?
}

data class Order(
    val id: Int,
    val desc: String,
    val price: Double,
    val data: LocalDate
)

data class GSorpresa(
    override val id: String?,
    override val title: String?,
    override val desc: String?,
    override val price: Double?,
    override val pic: String?
) : Product

data class Food(
    override val id: String?,
    override val title: String?,
    override val desc: String?,
    override val price: Double?,
    override val pic: String?,

    val category: Category?,
    val season: Seasons?
) : Product {
    enum class Seasons(private val displayName: String) {
        SPRING("spring"),
        SUMMER("summer"),
        AUTUMN("fall"),
        WINTER("winter");

        companion object {
            fun from(findValue: String): Seasons = values().first {it.displayName.equals(findValue, true)}
        }
        override fun toString(): String {
            return displayName
        }
    }

    enum class Category(private val displayName: String) {
        STARTER("starter"),
        DESSERT("dessert"),
        MAIN("main");

        companion object {
            fun from(findValue: String): Category = values().first { it.displayName.equals(findValue, true) }
        }
        override fun toString(): String {
            return displayName
        }
    }
}