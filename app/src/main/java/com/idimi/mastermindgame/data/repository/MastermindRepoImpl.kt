package com.idimi.mastermindgame.data.repository

import com.idimi.mastermindgame.data.dao.MastermindDAO
import com.idimi.mastermindgame.data.dao.StaticWordDAO
import com.idimi.mastermindgame.data.entity.MastermindResultEntity
import com.idimi.mastermindgame.data.entity.WordEntity
import com.idimi.mastermindgame.domain.model.MastermindResult
import com.idimi.mastermindgame.domain.model.MastermindWord
import com.idimi.mastermindgame.domain.repository.MastermindRepo
import com.idimi.mastermindgame.data.local.GameDictionary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MastermindRepoImpl(
    private val mastermindDAO: MastermindDAO,
    private val wordDao: StaticWordDAO,
    private val ioDispatcher: CoroutineDispatcher
): MastermindRepo {

    override suspend fun getSecretWord(): MastermindWord = withContext(ioDispatcher) {
        val count = wordDao.getCount()
        if (count == 0) {
            val entities =
                GameDictionary.wordsWithHints.map { WordEntity(word = it.key, hint = it.value) }
            wordDao.insertAll(entities)
        }
        val randomWordEntity = wordDao.getRandomWord()
        MastermindWord(randomWordEntity.word, randomWordEntity.hint)
    }

    override suspend fun getHallOfFameData(): List<MastermindResult> = withContext(ioDispatcher) {
        mastermindDAO.getTop10Results().map { MastermindResult(it.name, it.score, it.timestamp) }
    }

    override suspend fun saveNewScore(mastermindResult: MastermindResult) = withContext(ioDispatcher) {
        mastermindDAO.insert(
            MastermindResultEntity(
                id = 0,
                name = mastermindResult.name,
                score = mastermindResult.score,
                timestamp = mastermindResult.time
            )
        )
    }
}