package com.example.rpgolddragon.model

data class Atributos(
    val forca: Int = 0,
    val destreza: Int = 0,
    val constituicao: Int = 0,
    val inteligencia: Int = 0,
    val sabedoria: Int = 0,
    val carisma: Int = 0
) {
    fun getValor(nomeAtributo: String): Int {
        return when (nomeAtributo) {
            "FORÇA" -> forca
            "DESTREZA" -> destreza
            "CONSTITUIÇÃO" -> constituicao
            "INTELIGÊNCIA" -> inteligencia
            "SABEDORIA" -> sabedoria
            "CARISMA" -> carisma
            else -> 0
        }
    }
}