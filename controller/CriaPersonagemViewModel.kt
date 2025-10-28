package com.olddragon.front.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.olddragon.front.model.*
import com.olddragon.front.model.data.AppDatabase
import com.olddragon.front.model.data.PersonagemEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Mudança para AndroidViewModel para acesso ao Context/Application
class CriaPersonagemViewModel(application: Application) : AndroidViewModel(application) {

    // Inicialização do ROOM
    private val personagemDao = AppDatabase.getDatabase(application).personagemDao()

    // Estados Reativos do Personagem
    private val _metodoEscolhido = MutableStateFlow(GeradorAtributos.MetodoDistribuicao.CLASSICO)
    val metodoEscolhido: StateFlow<GeradorAtributos.MetodoDistribuicao> = _metodoEscolhido

    private val _atributosFinais = MutableStateFlow(Atributos())
    val atributosFinais: StateFlow<Atributos> = _atributosFinais

    private val _valoresDisponiveis = MutableStateFlow(emptyList<Int>())
    val valoresDisponiveis: StateFlow<List<Int>> = _valoresDisponiveis

    private val _racaSelecionada = MutableStateFlow(Racas.HUMANO)
    val racaSelecionada: StateFlow<Raca> = _racaSelecionada

    private val _classeSelecionada = MutableStateFlow(Classes.GUERREIRO)
    val classeSelecionada: StateFlow<Classe> = _classeSelecionada

    private val _mensagemUsuario = MutableStateFlow<String?>(null)
    val mensagemUsuario: StateFlow<String?> = _mensagemUsuario

    val todosAtributosNome = listOf("FOR", "DES", "CON", "INT", "SAB", "CAR")


    // Lógica: Geração e Distribuição
    fun setMetodo(metodo: GeradorAtributos.MetodoDistribuicao) {
        _metodoEscolhido.value = metodo
        gerarAtributosIniciais()
    }

    fun gerarAtributosIniciais() {
        val valores = GeradorAtributos.gerarValoresSimulados(_metodoEscolhido.value)

        if (_metodoEscolhido.value == GeradorAtributos.MetodoDistribuicao.CLASSICO) {
            // Clássico: Aloca em ordem fixa
            _atributosFinais.value = Atributos(
                forca = valores.getOrElse(0) { 0 },
                destreza = valores.getOrElse(1) { 0 },
                constituicao = valores.getOrElse(2) { 0 },
                inteligencia = valores.getOrElse(3) { 0 },
                sabedoria = valores.getOrElse(4) { 0 },
                carisma = valores.getOrElse(5) { 0 }
            )
            _valoresDisponiveis.value = emptyList()
        } else {
            // Aventureiro/Heróico: Zera e torna os valores disponíveis
            _atributosFinais.value = Atributos()
            _valoresDisponiveis.value = valores.toMutableList()
        }
        _mensagemUsuario.value = null // Limpa a mensagem ao gerar
    }

    fun alocarValor(atributoNome: String, valorAlocado: Int) {
        // 1. Remove o valor da lista de disponíveis
        val listaAtualizada = _valoresDisponiveis.value.toMutableList()
        if (!listaAtualizada.remove(valorAlocado)) {
            // Se o valor não estava disponível, não faça nada
            return
        }
        _valoresDisponiveis.value = listaAtualizada

        // 2. Atualiza o atributo no Atributos Finais
        val atts = _atributosFinais.value
        val novosAtts = when (atributoNome) {
            "FOR" -> atts.copy(forca = valorAlocado)
            "DES" -> atts.copy(destreza = valorAlocado)
            "CON" -> atts.copy(constituicao = valorAlocado)
            "INT" -> atts.copy(inteligencia = valorAlocado)
            "SAB" -> atts.copy(sabedoria = valorAlocado)
            "CAR" -> atts.copy(carisma = valorAlocado)
            else -> atts
        }
        _atributosFinais.value = novosAtts
    }

    fun setRaca(raca: Raca) {
        _racaSelecionada.value = raca
    }

    fun setClasse(classe: Classe) {
        _classeSelecionada.value = classe
    }

    // Lógica: Persistência (ROOM)
    fun salvarPersonagem(nomePersonagem: String) {
        if (_valoresDisponiveis.value.isNotEmpty()) {
            _mensagemUsuario.value = "Erro: Distribua todos os 6 atributos antes de salvar!"
            return
        }

        val nomeFinal = nomePersonagem.ifBlank { "Aventureiro Desconhecido" }

        val personagemParaSalvar = PersonagemEntity(
            nome = nomeFinal,
            raca = _racaSelecionada.value.nome,
            classe = _classeSelecionada.value.nome,
            forca = _atributosFinais.value.forca,
            destreza = _atributosFinais.value.destreza,
            constituicao = _atributosFinais.value.constituicao,
            inteligencia = _atributosFinais.value.inteligencia,
            sabedoria = _atributosFinais.value.sabedoria,
            carisma = _atributosFinais.value.carisma
        )

        viewModelScope.launch {
            val idSalvo = personagemDao.insert(personagemParaSalvar)
            _mensagemUsuario.value = "Personagem '$nomeFinal' salvo com sucesso! ID: $idSalvo"
        }
    }

    fun limparMensagem() {
        _mensagemUsuario.value = null
    }
}