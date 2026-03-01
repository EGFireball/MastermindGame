package com.idimi.mastermindgame.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.idimi.mastermindgame.data.entity.WordEntity

@Dao
interface StaticWordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<WordEntity>)

    @Query("SELECT * FROM dictionary_table ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): WordEntity

    @Query("SELECT COUNT(*) FROM dictionary_table")
    suspend fun getCount(): Int
}