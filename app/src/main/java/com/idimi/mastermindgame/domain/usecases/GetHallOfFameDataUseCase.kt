package com.idimi.mastermindgame.domain.usecases

import com.idimi.mastermindgame.domain.model.MastermindResult
import com.idimi.mastermindgame.domain.repository.MastermindRepo

class GetHallOfFameDataUseCase(val repo: MastermindRepo) {
    suspend operator fun invoke(): List<MastermindResult> =
        repo.getHallOfFameData()
}