package com.idimi.mastermindgame.domain.usecases

import com.idimi.mastermindgame.domain.model.MastermindWord
import com.idimi.mastermindgame.domain.repository.MastermindRepo

class GetSecretWordUseCase(val repo: MastermindRepo) {
    suspend operator fun invoke(): MastermindWord {
        return repo.getSecretWord()
    }
}