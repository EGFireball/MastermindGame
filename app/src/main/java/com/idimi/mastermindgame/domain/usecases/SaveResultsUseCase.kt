package com.idimi.mastermindgame.domain.usecases

import com.idimi.mastermindgame.domain.model.MastermindResult
import com.idimi.mastermindgame.domain.repository.MastermindRepo

class SaveResultsUseCase(val repo: MastermindRepo) {
    suspend operator fun invoke(mastermindResult: MastermindResult) =
        repo.saveNewScore(mastermindResult)
}