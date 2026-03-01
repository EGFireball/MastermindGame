package com.idimi.mastermindgame.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idimi.mastermindgame.data.dao.MastermindDAO
import com.idimi.mastermindgame.data.dao.StaticWordDAO
import com.idimi.mastermindgame.data.entity.WordEntity
import com.idimi.mastermindgame.data.entity.MastermindResultEntity

@Database(
    entities = [MastermindResultEntity::class, WordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MastermindDB: RoomDatabase() {
    abstract fun gameDao(): MastermindDAO
    abstract fun wordDao(): StaticWordDAO
}