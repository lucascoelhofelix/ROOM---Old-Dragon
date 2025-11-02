package com.example.rpgolddragon.model

import kotlin.random.Random

object GeradorAtributos {

    enum class MetodoDistribuicao {
        CLASSICO, // 3d6 em ordem (ViewModel já preenche a UI)
        CUSTOMIZADO // 4d6, descarta o menor, escolhe onde alocar (ViewModel preenche lista)
    }

    data class ResultadoGeracao(
        val atributos: Atributos? = null,
        val valores: List<Int>? = null
    )

    fun gerarValores(metodo: MetodoDistribuicao): ResultadoGeracao {
        return when (metodo) {
            MetodoDistribuicao.CLASSICO -> {
                val forca = rolar3d6()
                val destreza = rolar3d6()
                val constituicao = rolar3d6()
                val inteligencia = rolar3d6()
                val sabedoria = rolar3d6()
                val carisma = rolar3d6()
                ResultadoGeracao(
                    atributos = Atributos(
                        forca, destreza, constituicao, inteligencia, sabedoria, carisma
                    )
                )
            }
            MetodoDistribuicao.CUSTOMIZADO -> {
                val valores = mutableListOf<Int>()
                repeat(6) {
                    valores.add(rolar4d6DescartarMenor())
                }
                ResultadoGeracao(valores = valores.sortedDescending())
            }
        }
    }

    private fun rolar3d6(): Int {
        return Random.nextInt(1, 7) + Random.nextInt(1, 7) + Random.nextInt(1, 7)
    }

    private fun rolar4d6DescartarMenor(): Int {
        val rolagens = mutableListOf<Int>()
        repeat(4) {
            rolagens.add(Random.nextInt(1, 7))
        }
        rolagens.removeAt(rolagens.indexOf(rolagens.minOrNull()))
        return rolagens.sum()
    }
}