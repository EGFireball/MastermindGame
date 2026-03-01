package com.idimi.mastermindgame.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary_table")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,
    val hint: String
)