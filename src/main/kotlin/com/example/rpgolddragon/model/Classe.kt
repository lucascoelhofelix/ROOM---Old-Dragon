package com.example.rpgolddragon.model

data class Classe(val nome: String, val descricao: String)

object Classes {
    val GUERREIRO = Classe("Guerreiro", "Mestres em combate armado e armaduras pesadas.")
    val CLERIGO = Classe("Clérigo", "Curadores e guerreiros sagrados, usam magia divina.")
    val LADINO = Classe("Ladino", "Especialistas em furtividade, arrombamento e armadilhas.")
    val MAGO = Classe("Mago", "Conjuradores de magias arcanas, fracos no corpo-a-corpo.")

    val todas: List<Classe> = listOf(GUERREIRO, CLERIGO, LADINO, MAGO)
}