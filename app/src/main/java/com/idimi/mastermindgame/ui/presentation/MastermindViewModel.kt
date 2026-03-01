package com.idimi.mastermindgame.ui.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idimi.mastermindgame.domain.model.MastermindResult
import com.idimi.mastermindgame.domain.model.MastermindWord
import com.idimi.mastermindgame.domain.usecases.CheckMastermindUseCase
import com.idimi.mastermindgame.domain.usecases.GetHallOfFameDataUseCase
import com.idimi.mastermindgame.domain.usecases.GetSecretWordUseCase
import com.idimi.mastermindgame.domain.usecases.GuessResult
import com.idimi.mastermindgame.domain.usecases.SaveResultsUseCase
import com.idimi.mastermindgame.ui.theme.NeonGreen
import com.idimi.mastermindgame.ui.theme.NeonOrange
import com.idimi.mastermindgame.ui.theme.NeonRed
import com.idimi.mastermindgame.utils.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class MastermindViewModel(
    private val getSecretWordUseCase: GetSecretWordUseCase,
    private val getHallOfFameDataUseCase: GetHallOfFameDataUseCase,
    private val saveResultsUseCase: SaveResultsUseCase,
    private val checkMastermindUseCase: CheckMastermindUseCase
) : ViewModel() {

    private val _secretWord = MutableStateFlow<MastermindWord>(MastermindWord("", ""))
    val secretWord = _secretWord.asStateFlow()

    private val _highScores = MutableStateFlow<List<MastermindResult>>(emptyList())
    val highScores = _highScores.asStateFlow()

    private val _gameEvents = MutableSharedFlow<GameEvent>()
    val gameEvents = _gameEvents.asSharedFlow()

    private val _timeLeft = MutableStateFlow(Constants.GAME_TIMER_SECONDS)
    val timeLeft = _timeLeft.asStateFlow()

    private var timerJob: Job? = null

    fun startTimer(onTimeUp: () -> Unit) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            (Constants.GAME_TIMER_SECONDS downTo 0).asFlow()
                .onEach { delay(Constants.TIMER_DELAY_MS) }
                .collect { tick ->
                    _timeLeft.value = tick
                    if (tick == 0) {
                        onTimeUp()
                    }
                }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun resetTimer() {
        stopTimer()
        _timeLeft.value = Constants.GAME_TIMER_SECONDS
    }

    fun loadNewGame() {
        viewModelScope.launch {
            val word = getSecretWordUseCase()
            Timber.d("Secret_word: ${word.word}")
            _secretWord.value = word
        }
    }

    fun getHighScores()  {
        viewModelScope.launch {
            _highScores.value = getHallOfFameDataUseCase.invoke()
        }
    }

    fun saveScore(playerName: String, score: Int) {
        viewModelScope.launch {
            saveResultsUseCase.invoke(
                MastermindResult(
                    name = playerName,
                    score = score
                )
            )
        }
    }

    fun checkMastermindLogic(guess: String, secret: String): List<Color> {
        return checkMastermindUseCase.invoke(guess, secret).map {
            when (it) {
                GuessResult.NONE -> Color.LightGray
                GuessResult.CORRECT -> NeonGreen
                GuessResult.WRONG_POSITION -> NeonOrange
                GuessResult.INCORRECT -> NeonRed
            }
        }
    }

    fun calculateScore(correctLetters: Int, remainingSeconds: Int): Int {
        val letterPoints = correctLetters * Constants.POINTS_PER_LETTER
        val timeBonus = remainingSeconds * Constants.TIME_BONUS_MULTIPLIER
        return letterPoints + timeBonus
    }

    fun triggerVictoryEvent() {
        viewModelScope.launch {
            _gameEvents.emit(GameEvent.OnVictory)
        }
    }

    fun triggerGameOverEvent() {
        viewModelScope.launch {
            _gameEvents.emit(GameEvent.OnGameOver)
        }
    }

    sealed class GameEvent {
        object OnVictory : GameEvent()
        object OnGameOver : GameEvent()
    }
}
