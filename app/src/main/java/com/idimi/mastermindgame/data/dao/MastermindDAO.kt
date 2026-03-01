package com.idimi.mastermindgame.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.idimi.mastermindgame.data.entity.MastermindResultEntity

@Dao
interface MastermindDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameResult: MastermindResultEntity)

    @Update
    suspend fun update(gameResult: MastermindResultEntity)

    @Query("SELECT * FROM game_results ORDER BY score DESC LIMIT 10")
    suspend fun getTop10Results(): List<MastermindResultEntity>

}