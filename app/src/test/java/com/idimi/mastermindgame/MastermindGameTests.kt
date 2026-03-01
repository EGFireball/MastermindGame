package com.idimi.mastermindgame

import app.cash.turbine.test
import com.idimi.mastermindgame.domain.model.MastermindWord
import com.idimi.mastermindgame.domain.usecases.CheckMastermindUseCase
import com.idimi.mastermindgame.domain.usecases.GetHallOfFameDataUseCase
import com.idimi.mastermindgame.domain.usecases.GetSecretWordUseCase
import com.idimi.mastermindgame.domain.usecases.GuessResult
import com.idimi.mastermindgame.domain.usecases.SaveResultsUseCase
import com.idimi.mastermindgame.ui.presentation.MastermindViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutineTestExtension::class)
class MastermindGameTests {

    // Dependencies
    private val getSecretWordUseCase = mockk<GetSecretWordUseCase>()
    private val getHallOfFameDataUseCase = mockk<GetHallOfFameDataUseCase>()
    private val saveResultsUseCase = mockk<SaveResultsUseCase>()
    private val checkMastermindUseCase = CheckMastermindUseCase()

    private lateinit var viewModel: MastermindViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        viewModel = MastermindViewModel(
            getSecretWordUseCase,
            getHallOfFameDataUseCase,
            saveResultsUseCase,
            checkMastermindUseCase
        )
    }

    @Test
    fun guessIsCorrect() {
        val result = checkMastermindUseCase("WORD", "WORD")
        assertTrue(result.all { it == GuessResult.CORRECT })
    }

    @Test
    fun correctLatersWrongPositions() {
        val result = checkMastermindUseCase("DCBA", "ABCD")
        assertTrue(result.all { it == GuessResult.WRONG_POSITION })
    }

    @Test
    fun whenDuplicateLettersInGuessOnlyFirstIsGreenIfOneInSecret() {
        val result = checkMastermindUseCase("AAAA", "ABCC")
        assertEquals(GuessResult.CORRECT, result[0])
        assertEquals(GuessResult.INCORRECT, result[1])
    }

    @Test
    fun whenGuessIsEntirelyWrongReturnAllINCORRECT() {
        val result = checkMastermindUseCase("ZZZZ", "ABCD")
        assertTrue(result.all { it == GuessResult.INCORRECT })
    }

    @Test
    fun startNewGameUpdatesSecretWord() = runTest {
        val mockWord = MastermindWord("JAVA", "Coding language")
        // Mock response
        coEvery { getSecretWordUseCase() } returns mockWord

        viewModel.loadNewGame()

        // IMPORTANT:  advanceUntilIdle() will assure that each of the previous coroutines will finish
        // This prevents race conditions and issues, caused by concurrency.
        advanceUntilIdle()

        assertEquals(mockWord, viewModel.secretWord.value)
    }

    @Test
    fun timerTriggersOnTimeUpWhenReachingZero() = runTest {
        var isTimeUpCalled = false

        // Start the timer
        viewModel.startTimer { isTimeUpCalled = true }

        // We have 60 seconds but we will fast forward 61 seconds.
        advanceTimeBy(61_000)

        // Be sure that last task has been performed
        runCurrent()

        assertTrue(isTimeUpCalled, "Callback should have been called after 60 seconds")
    }

    @Test
    fun timerShouldEmitDescendingValues() = runTest {
        viewModel.timeLeft.test {
            viewModel.startTimer { }
            assertEquals(60, awaitItem())

            delay(1001)
            assertEquals(59, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun stopTimerStopsTimeDecrementing() = runTest {
        viewModel.startTimer { }
        advanceTimeBy(2000)
        val timeBeforeStop = viewModel.timeLeft.value

        viewModel.stopTimer()
        advanceTimeBy(5000)

        assertEquals(timeBeforeStop, viewModel.timeLeft.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveScoreInvokesRepositoryExactlyOnce() = runTest {
        coEvery { saveResultsUseCase(any()) } returns Unit

        viewModel.saveScore("Dimi", 5000)
        advanceUntilIdle()

        coVerify(exactly = 1) { saveResultsUseCase(any()) }
    }

    @Test
    fun calculateScoreMathIsCorrect() {
        val expected = 1200
        val actual = viewModel.calculateScore(2, 10)
        assertEquals(expected, actual)
    }
}
