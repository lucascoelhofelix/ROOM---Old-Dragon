package com.olddragon.front.model

data class Atributos(
    val forca: Int = 0,
    val destreza: Int = 0,
    val constituicao: Int = 0,
    val inteligencia: Int = 0,
    val sabedoria: Int = 0,
    val carisma: Int = 0
)

fun Atributos.getValorByNome(nome: String): Int {
    return when (nome) {
        "FOR" -> this.forca
        "DES" -> this.destreza
        "CON" -> this.constituicao
        "INT" -> this.inteligencia
        "SAB" -> this.sabedoria
        "CAR" -> this.carisma
        else -> 0
    }
}