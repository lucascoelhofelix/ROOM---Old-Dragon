package com.olddragon.front.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olddragon.front.controller.CriaPersonagemViewModel
import com.olddragon.front.model.*

@Composable
fun CriaPersonagemScreen(viewModel: CriaPersonagemViewModel = viewModel()) {

    // Observa o estado do ViewModel
    val metodo by viewModel.metodoEscolhido.collectAsState()
    val atributosFinais by viewModel.atributosFinais.collectAsState()
    val valoresDisponiveis by viewModel.valoresDisponiveis.collectAsState()
    val raca by viewModel.racaSelecionada.collectAsState()
    val classe by viewModel.classeSelecionada.collectAsState()
    val mensagemUsuario by viewModel.mensagemUsuario.collectAsState()

    val todosAtributosNome = viewModel.todosAtributosNome
    var nomePersonagem by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Exibe Toast ao receber mensagem de feedback
    LaunchedEffect(mensagemUsuario) {
        mensagemUsuario?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.limparMensagem() // Limpa a mensagem após exibir
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Front - Old Dragon") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. SELEÇÃO DO MÉTODO DE DISTRIBUIÇÃO
            SectionTitle(title = "1. Método de Atributos")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GeradorAtributos.MetodoDistribuicao.entries.forEach { m ->
                    FilterChip(
                        selected = metodo == m,
                        onClick = { viewModel.setMetodo(m) },
                        label = { Text(m.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 2. ATRIBUTOS
            SectionTitle(title = "2. Atributos Gerados")

            if (metodo == GeradorAtributos.MetodoDistribuicao.CLASSICO) {
                Text(text = "Método Clássico (Ordem Fixa)", Modifier.padding(bottom = 8.dp))
                AtributosLista(atributos = atributosFinais)
            } else {
                Text(text = "Método ${metodo.name} (Distribua os Valores)", Modifier.padding(bottom = 8.dp))
                ValoresDisponiveisChipGroup(valores = valoresDisponiveis)
                Spacer(modifier = Modifier.height(16.dp))

                DistribuicaoAtributosUI(
                    atributosFinais = atributosFinais,
                    valoresDisponiveis = valoresDisponiveis,
                    atributosNome = todosAtributosNome,
                    alocarValor = viewModel::alocarValor
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 3. SELEÇÃO DE RAÇA
            SectionTitle(title = "3. Seleção de Raça")
            RacaClasseSelector(
                itens = Racas.TODAS.map { it.nome },
                selecionado = raca.nome,
                onSelect = { nome -> viewModel.setRaca(Racas.TODAS.first { it.nome == nome }) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 4. SELEÇÃO DE CLASSE
            SectionTitle(title = "4. Seleção de Classe")
            RacaClasseSelector(
                itens = Classes.TODAS.map { it.nome },
                selecionado = classe.nome,
                onSelect = { nome -> viewModel.setClasse(Classes.TODAS.first { it.nome == nome }) }
            )
            Spacer(modifier = Modifier.weight(1f)) // Empurra os itens para cima

            // 5. NOME E SAVE
            OutlinedTextField(
                value = nomePersonagem,
                onValueChange = { nomePersonagem = it },
                label = { Text("Nome do Personagem") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Button(
                onClick = { viewModel.salvarPersonagem(nomePersonagem) },
                enabled = valoresDisponiveis.isEmpty(), // Habilita o botão se todos os 6 atributos foram alocados
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Personagem no Banco (ROOM)")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { viewModel.gerarAtributosIniciais() }, modifier = Modifier.fillMaxWidth()) {
                Text("Gerar/Rerolar Atributos")
            }
        }
    }
}

// --- Componentes UI Reutilizáveis (para clareza) ---

@Composable
fun SectionTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun RacaClasseSelector(itens: List<String>, selecionado: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
        itens.forEach { item ->
            FilterChip(
                selected = selecionado == item,
                onClick = { onSelect(item) },
                label = { Text(item) }
            )
        }
    }
}

@Composable
fun AtributosLista(atributos: Atributos) {
    Column(horizontalAlignment = Alignment.Start) {
        Text("FOR: ${atributos.forca}")
        Text("DES: ${atributos.destreza}")
        Text("CON: ${atributos.constituicao}")
        Text("INT: ${atributos.inteligencia}")
        Text("SAB: ${atributos.sabedoria}")
        Text("CAR: ${atributos.carisma}")
    }
}

@Composable
fun ValoresDisponiveisChipGroup(valores: List<Int>) {
    Text("Valores Restantes para Alocar: ${valores.sortedDescending().joinToString()}")
}

@Composable
fun DistribuicaoAtributosUI(
    atributosFinais: Atributos,
    valoresDisponiveis: List<Int>,
    atributosNome: List<String>,
    alocarValor: (String, Int) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        atributosNome.forEach { nome ->
            val valorAtual = atributosFinais.getValorByNome(nome)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "$nome:", modifier = Modifier.weight(1f))

                if (valorAtual == 0 && valoresDisponiveis.isNotEmpty()) {
                    // Dropdown para alocar
                    var expanded by remember { mutableStateOf(false) }

                    OutlinedButton(onClick = { expanded = true }) {
                        Text("Alocar")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        valoresDisponiveis.sortedDescending().forEach { valor ->
                            DropdownMenuItem(
                                text = { Text(valor.toString()) },
                                onClick = {
                                    alocarValor(nome, valor)
                                    expanded = false
                                }
                            )
                        }
                    }
                } else if (valorAtual != 0) {
                    // Valor já alocado
                    Text(text = "Alocado: $valorAtual", style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary))
                } else {
                    Text(text = "Aguardando Alocação")
                }
            }
            Divider()
        }
    }
}