package com.example.rpgolddragon.view

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rpgolddragon.model.Atributos // Importe sua classe Atributos
import com.example.rpgolddragon.model.getValor // Importe a função de extensão

// Extensão para compatibilidade com a nova classe Atributos (se for o caso)
fun Atributos.getValorByNome(nome: String): Int {
    return when (nome) {
        "FORÇA" -> forca
        "DESTREZA" -> destreza
        "CONSTITUIÇÃO" -> constituicao
        "INTELIGÊNCIA" -> inteligencia
        "SABEDORIA" -> sabedoria
        "CARISMA" -> carisma
        else -> 0
    }
}


// Título de Seção Estilizado
@Composable
fun SectionTitle(title: String) {
    Text(
        text = "— $title —",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
}

// Seletor de Chip Estilizado (Raça/Classe/Método)
@Composable
fun StyledChipSelector(itens: List<String>, selecionado: String, onSelect: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp)
    ) {
        itens.forEach { item ->
            val isSelected = selecionado == item
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(item) },
                label = { Text(item, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondary, // AccentBlood
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.tertiary, // SecondaryParchment
                    labelColor = MaterialTheme.colorScheme.primary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = MaterialTheme.colorScheme.primary,
                    selectedBorderColor = MaterialTheme.colorScheme.secondary,
                    borderWidth = 1.dp
                )
            )
        }
    }
}

// Card de Exibição de Atributos (Clássico)
@Composable
fun AtributosCard(atributos: Atributos) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Column(Modifier.padding(16.dp)) {
            AtributoLinha("FORÇA", atributos.forca)
            AtributoLinha("DESTREZA", atributos.destreza)
            AtributoLinha("CONSTITUIÇÃO", atributos.constituicao)
            Divider(Modifier.padding(vertical = 4.dp))
            AtributoLinha("INTELIGÊNCIA", atributos.inteligencia)
            AtributoLinha("SABEDORIA", atributos.sabedoria)
            AtributoLinha("CARISMA", atributos.carisma)
        }
    }
}

@Composable
fun AtributoLinha(nome: String, valor: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = nome, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        // Destaque se o valor for alto (Ex: > 14) ou muito baixo
        val corValor = if (valor > 14 || valor < 7 && valor != 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
        Text(text = valor.toString(), color = corValor, fontWeight = FontWeight.Bold)
    }
}

// Interface de Alocação de Atributos (Aventureiro/Heróico)
@Composable
fun DistribuicaoAtributosCard(
    atributosFinais: Atributos,
    valoresDisponiveis: List<Int>,
    atributosNome: List<String>,
    alocarValor: (String, Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Valores Restantes: ${valoresDisponiveis.sortedDescending().joinToString()}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            atributosNome.forEach { nome ->
                val valorAtual = atributosFinais.getValorByNome(nome)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = nome, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)

                    if (valorAtual == 0 && valoresDisponiveis.isNotEmpty()) {
                        var expanded by remember { mutableStateOf(false) }

                        Button(
                            onClick = { expanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Escolher Valor")
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
                        Text(text = valorAtual.toString(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text(text = "–", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Divider()
            }
        }
    }
}