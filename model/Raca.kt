package com.olddragon.front.model

data class Raca(
    val nome: String,
    val descricao: String
)

object Racas {
    val HUMANO = Raca("Humano", "Versátil e sem restrições de classe.")
    val ANAO = Raca("Anão", "Resistente, visão no escuro e bônus de localização.")
    val ELFO = Raca("Elfo", "Habilidoso com magias, visão no escuro e imune a Sono.")
    val TODAS = listOf(HUMANO, ANAO, ELFO)
}