package com.idimi.mastermindgame.domain.usecases

enum class GuessResult {
    NONE,
    CORRECT,      // Green
    WRONG_POSITION, // Orange
    INCORRECT,     // Red
}

class CheckMastermindUseCase {
    operator fun invoke(guess: String, secret: String): List<GuessResult> {
        val result = MutableList(4) { GuessResult.INCORRECT }
        val secretList = secret.uppercase().toList().toMutableList()
        val guessList = guess.uppercase().toList()

        // 1. CORRECT (Green)
        for (i in 0..3) {
            if (guessList[i] == secretList[i]) {
                result[i] = GuessResult.CORRECT
                secretList[i] = ' '
            }
        }

        // 2. WRONG_POSITION (Orange)
        for (i in 0..3) {
            if (result[i] != GuessResult.CORRECT) {
                val indexInSecret = secretList.indexOf(guessList[i])
                if (indexInSecret != -1) {
                    result[i] = GuessResult.WRONG_POSITION
                    secretList[indexInSecret] = ' '
                }
            }
        }
        return result
    }
}
