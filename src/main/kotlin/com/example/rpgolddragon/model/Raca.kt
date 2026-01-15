package com.example.rpgolddragon.model

data class Raca(val nome: String, val descricao: String)

object Racas {
    val HUMANO = Raca("Humano", "Versáteis e adaptáveis, recebem bônus em experiência.")
    val ELFO = Raca("Elfo", "Hábeis com arco, imunes a magias de sono e paralisia.")
    val ANAO = Raca("Anão", "Resistentes e fortes, bônus contra venenos e magias.")
    val HALFLING = Raca("Halfling", "Pequenos e sortudos, bônus em testes de Destreza.")

    val todas: List<Raca> = listOf(HUMANO, ELFO, ANAO, HALFLING)
}