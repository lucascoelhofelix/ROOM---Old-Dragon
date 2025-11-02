package com.example.rpgolddragon.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rpgolddragon.controller.CriaPersonagemViewModel
import com.example.rpgolddragon.model.Classes
import com.example.rpgolddragon.model.GeradorAtributos
import com.example.rpgolddragon.model.Racas
// Não precisa importar o tema aqui, pois o MainActivity já o aplica

@Composable
fun CriaPersonagemScreen(viewModel: CriaPersonagemViewModel) {
    // Observação do Estado Único
    val uiState by viewModel.uiState.collectAsState()

    val metodo = uiState.metodoEscolhido
    val atributosFinais = uiState.atributosFinais
    val valoresDisponiveis = uiState.valoresDisponiveis
    val raca = uiState.racaSelecionada
    val classe = uiState.classeSelecionada
    val mensagemUsuario = uiState.mensagemUsuario

    val todosAtributosNome = viewModel.todosAtributosNome
    var nomePersonagem by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Feedback do ROOM (Toast)
    LaunchedEffect(mensagemUsuario) {
        mensagemUsuario?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.limparMensagem()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ficha Old Dragon") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. MÉTODO DE ATRIBUTOS
            SectionTitle(title = "1. Geração de Atributos")
            StyledChipSelector(
                itens = GeradorAtributos.MetodoDistribuicao.entries.map { it.name },
                selecionado = metodo.name,
                onSelect = { nome ->
                    viewModel.setMetodo(GeradorAtributos.MetodoDistribuicao.valueOf(nome))
                }
            )

            // Botão de Rerolar Atributos
            Button(
                onClick = { viewModel.gerarAtributosIniciais() },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Rerolar Atributos")
            }

            // 2. ATRIBUTOS
            if (metodo == GeradorAtributos.MetodoDistribuicao.CLASSICO) {
                AtributosCard(atributos = atributosFinais)
            } else {
                DistribuicaoAtributosCard(
                    atributosFinais = atributosFinais,
                    valoresDisponiveis = valoresDisponiveis,
                    atributosNome = todosAtributosNome,
                    alocarValor = viewModel::alocarValor
                )
            }

            // 3. RAÇA
            SectionTitle(title = "2. Escolha de Raça")
            StyledChipSelector(
                itens = Racas.todas.map { it.nome },
                selecionado = raca.nome,
                onSelect = { nome -> viewModel.setRaca(Racas.todas.first { it.nome == nome }) }
            )

            // 4. CLASSE
            SectionTitle(title = "3. Escolha de Classe")
            StyledChipSelector(
                itens = Classes.todas.map { it.nome },
                selecionado = classe.nome,
                onSelect = { nome -> viewModel.setClasse(Classes.todas.first { it.nome == nome }) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5. NOME E SALVAMENTO
            SectionTitle(title = "4. Salvar Personagem")
            OutlinedTextField(
                value = nomePersonagem,
                onValueChange = { nomePersonagem = it },
                label = { Text("Nome do Herói") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Button(
                onClick = { viewModel.salvarPersonagem(nomePersonagem) },
                // Habilita se for clássico OU se for customizado e todos os valores foram usados
                enabled = metodo == GeradorAtributos.MetodoDistribuicao.CLASSICO || valoresDisponiveis.isEmpty(),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Filled.Save, contentDescription = "Salvar")
                Spacer(Modifier.width(8.dp))
                Text("SALVAR FICHA NO BANCO (ROOM)")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}