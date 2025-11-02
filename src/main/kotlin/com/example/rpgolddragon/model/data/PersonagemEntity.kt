package com.example.rpgolddragon.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personagens")
data class PersonagemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val raca: String,
    val classe: String,
    val forca: Int,
    val destreza: Int,
    val constituicao: Int,
    val inteligencia: Int,
    val sabedoria: Int,
    val carisma: Int,
    val dataCriacao: Long = System.currentTimeMillis()
)