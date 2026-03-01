package com.idimi.mastermindgame.domain.repository

import com.idimi.mastermindgame.domain.model.MastermindResult
import com.idimi.mastermindgame.domain.model.MastermindWord

interface MastermindRepo {
    suspend fun getSecretWord(): MastermindWord
    suspend fun getHallOfFameData(): List<MastermindResult>
    suspend fun saveNewScore(mastermindResult: MastermindResult)
}