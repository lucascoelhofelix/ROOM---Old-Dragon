package com.olddragon.front.model

object GeradorAtributos {

    enum class MetodoDistribuicao {
        CLASSICO, AVENTUREIRO, HEROICO
    }

    // Função de Geração de Valores (Simulada para fins de projeto)
    fun gerarValoresSimulados(metodo: MetodoDistribuicao): List<Int> {
        // Implementação real exigiria rolagem de dados (Random)
        return when (metodo) {
            MetodoDistribuicao.CLASSICO -> listOf(12, 10, 8, 14, 11, 9)
            MetodoDistribuicao.AVENTUREIRO -> listOf(15, 13, 11, 9, 7, 5) // Valores para distribuir
            MetodoDistribuicao.HEROICO -> listOf(18, 16, 14, 12, 10, 8)    // Valores para distribuir
        }
    }
}