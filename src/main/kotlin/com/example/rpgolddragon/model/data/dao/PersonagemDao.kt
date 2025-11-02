package com.example.rpgolddragon.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rpgolddragon.model.data.PersonagemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonagemDao {
    @Insert(onConflictStrategy = OnConflictStrategy.REPLACE)
    suspend fun insert(personagem: PersonagemEntity): Long

    @Query("SELECT * FROM personagens ORDER BY dataCriacao DESC")
    fun getAll(): Flow<List<PersonagemEntity>>
}