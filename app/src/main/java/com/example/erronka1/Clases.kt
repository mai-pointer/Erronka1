package com.example.erronka1

data class Proveedor(
    var nombre: String,
    var descripcion: String,
    var productos: List<String>
)

interface Product {
    val id: Int
    val title: String
    val desc: String
    val price: Double
    val pic: String
}

data class GSorpresa(
    override val id: Int,
    override val title: String,
    override val desc: String,
    override val price: Double,
    override val pic: String
) : Product

data class Food(
    override val id: Int,
    override val title: String,
    override val desc: String,
    override val price: Double,
    override val pic: String,

    val category: Category,
    val season: Seasons
) : Product {
    enum class Seasons(private val displayName: String) {
        SPRING("Primavera"),
        SUMMER("Verano"),
        AUTUMN("Otoño"),
        WINTER("Invierno");

        override fun toString(): String {
            return displayName
        }
    }

    enum class Category(private val displayName: String) {
        STARTER("Entrante"),
//        DESSERT("Postre"),
        MAIN("Plato principal");

        override fun toString(): String {
            return displayName
        }
    }
}