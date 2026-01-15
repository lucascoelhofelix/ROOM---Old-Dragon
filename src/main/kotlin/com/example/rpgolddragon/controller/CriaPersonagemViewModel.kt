package com.example.rpgolddragon.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpgolddragon.model.* // Importe todas as suas classes de Model
import com.example.rpgolddragon.model.data.AppDatabase
import com.example.rpgolddragon.model.data.PersonagemEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CriaPersonagemUiState(
    val atributosFinais: Atributos = Atributos(),
    val valoresDisponiveis: List<Int> = emptyList(),
    val racaSelecionada: Raca = Racas.HUMANO,
    val classeSelecionada: Classe = Classes.GUERREIRO,
    val metodoEscolhido: GeradorAtributos.MetodoDistribuicao = GeradorAtributos.MetodoDistribuicao.CLASSICO,
    val mensagemUsuario: String? = null // Para feedback do ROOM
)

class CriaPersonagemViewModel(application: Application) : AndroidViewModel(application) {

    // Acesso ao ROOM DAO
    private val personagemDao = AppDatabase.getDatabase(application).personagemDao()

    // Estado da UI
    private val _uiState = MutableStateFlow(CriaPersonagemUiState())
    val uiState: StateFlow<CriaPersonagemUiState> = _uiState

    // Nomes dos atributos para a UI (simplificado)
    val todosAtributosNome = listOf("FORÇA", "DESTREZA", "CONSTITUIÇÃO", "INTELIGÊNCIA", "SABEDORIA", "CARISMA")

    init {
        // Gera atributos ao iniciar (usando o método CLÁSSICO por padrão)
        gerarAtributosIniciais()
    }

    // --- LÓGICA DE GERAÇÃO E ALOCAÇÃO DE ATRIBUTOS ---

    fun setMetodo(metodo: GeradorAtributos.MetodoDistribuicao) {
        _uiState.update { it.copy(metodoEscolhido = metodo) }
        gerarAtributosIniciais()
    }

    fun gerarAtributosIniciais() {
        val metodo = _uiState.value.metodoEscolhido
        val resultado = GeradorAtributos.gerarValores(metodo)

        _uiState.update {
            if (metodo == GeradorAtributos.MetodoDistribuicao.CLASSICO) {
                it.copy(
                    atributosFinais = resultado.atributos!!,
                    valoresDisponiveis = emptyList()
                )
            } else {
                it.copy(
                    atributosFinais = Atributos(), // Zera para redistribuir
                    valoresDisponiveis = resultado.valores!!
                )
            }
        }
    }

    fun alocarValor(nomeAtributo: String, valor: Int) {
        val estado = _uiState.value
        if (estado.valoresDisponiveis.contains(valor)) {

            // Remove o valor da lista de disponíveis
            val novaLista = estado.valoresDisponiveis.toMutableList()
            novaLista.remove(valor)

            // Cria o novo objeto Atributos
            val novosAtributos = estado.atributosFinais.copy(
                forca = if (nomeAtributo == "FORÇA") valor else estado.atributosFinais.forca,
                destreza = if (nomeAtributo == "DESTREZA") valor else estado.atributosFinais.destreza,
                constituicao = if (nomeAtributo == "CONSTITUIÇÃO") valor else estado.atributosFinais.constituicao,
                inteligencia = if (nomeAtributo == "INTELIGÊNCIA") valor else estado.atributosFinais.inteligencia,
                sabedoria = if (nomeAtributo == "SABEDORIA") valor else estado.atributosFinais.sabedoria,
                carisma = if (nomeAtributo == "CARISMA") valor else estado.atributosFinais.carisma
            )

            _uiState.update {
                it.copy(
                    atributosFinais = novosAtributos,
                    valoresDisponiveis = novaLista
                )
            }
        }
    }

    // --- LÓGICA DE SELEÇÃO (RAÇA/CLASSE) ---

    fun setRaca(raca: Raca) {
        _uiState.update { it.copy(racaSelecionada = raca) }
    }

    fun setClasse(classe: Classe) {
        _uiState.update { it.copy(classeSelecionada = classe) }
    }

    // --- LÓGICA DE PERSISTÊNCIA (ROOM) ---

    fun salvarPersonagem(nomePersonagem: String) {
        val estado = _uiState.value

        if (nomePersonagem.isBlank()) {
            _uiState.update { it.copy(mensagemUsuario = "O nome do personagem não pode ser vazio.") }
            return
        }

        // Verifica se todos os atributos foram alocados nos métodos não-clássicos
        if (estado.metodoEscolhido != GeradorAtributos.MetodoDistribuicao.CLASSICO && estado.valoresDisponiveis.isNotEmpty()) {
            _uiState.update { it.copy(mensagemUsuario = "Complete a alocação de todos os atributos.") }
            return
        }

        // Mapeamento (Model -> Entity)
        val personagemEntity = PersonagemEntity(
            nome = nomePersonagem,
            raca = estado.racaSelecionada.nome,
            classe = estado.classeSelecionada.nome,
            forca = estado.atributosFinais.forca,
            destreza = estado.atributosFinais.destreza,
            constituicao = estado.atributosFinais.constituicao,
            inteligencia = estado.atributosFinais.inteligencia,
            sabedoria = estado.atributosFinais.sabedoria,
            carisma = estado.atributosFinais.carisma
        )

        // Chamada assíncrona para o DAO
        viewModelScope.launch {
            try {
                val idSalvo = personagemDao.insert(personagemEntity)
                _uiState.update { it.copy(mensagemUsuario = "Personagem '$nomePersonagem' salvo com sucesso! ID: $idSalvo") }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensagemUsuario = "Erro ao salvar: ${e.message}") }
            }
        }
    }

    fun limparMensagem() {
        _uiState.update { it.copy(mensagemUsuario = null) }
    }
}