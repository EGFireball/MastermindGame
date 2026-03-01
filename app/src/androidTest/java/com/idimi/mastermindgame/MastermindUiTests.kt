package com.idimi.mastermindgame

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.idimi.mastermindgame.domain.model.MastermindWord
import com.idimi.mastermindgame.ui.presentation.MastermindViewModel
import com.idimi.mastermindgame.ui.presentation.compose.GameOverScreen
import com.idimi.mastermindgame.ui.presentation.compose.HallOfFameScreen
import com.idimi.mastermindgame.ui.presentation.compose.MastermindScreen
import com.idimi.mastermindgame.ui.presentation.compose.MenuScreen
import com.idimi.mastermindgame.ui.presentation.compose.SuccessScreen
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MastermindUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    // 1. MenuScreen test - Check for buttons and navigation
    @Test
    fun menuScreenShowsAllButtons() {
        composeTestRule.setContent {
            MenuScreen(onNewGame = {}, onShowHallOfFame = {}, onQuit = {})
        }

        // Check if resource texts are on the screen
        // Use activity context to access strings
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val newGameText = context.getString(R.string.new_game)
        val hallOfFameText = context.getString(R.string.hall_of_fame)

        composeTestRule.onNodeWithText(newGameText).assertIsDisplayed()
        composeTestRule.onNodeWithText(hallOfFameText).assertIsDisplayed()
    }

    // 2. SuccessScreen test - Check button click logic
    @Test
    fun successScreenNewGameClickTriggersCallback() {
        var clicked = false
        composeTestRule.setContent {
            SuccessScreen(newGame = { clicked = true }, onMenu = {})
        }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val newGameText = context.getString(R.string.new_game)

        composeTestRule.onNodeWithText(newGameText).performClick()
        assert(clicked)
    }

    // 3. GameOverScreen test - Check error visual element
    @Test
    fun gameOverScreenDisplaysErrorText() {
        composeTestRule.setContent {
            GameOverScreen(onRetry = {}, onMenu = {})
        }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val gameOverText = context.getString(R.string.game_over)

        // Check if "GAME OVER" text is present
        composeTestRule.onNodeWithText(gameOverText).assertExists()
    }

    // 4. Test MastermindScreen
    @Test
    fun mastermindScreenInputUpdatingIsReflected() {
        val viewModel = mockk<MastermindViewModel>(relaxed = true)

        // Use MutableStateFlow so the 'by' delegate has a real value to extract
        every { viewModel.secretWord } returns MutableStateFlow(MastermindWord("WORD", "Hint"))
        every { viewModel.timeLeft } returns MutableStateFlow(60)

        // This prevents the ClassCastException when the UI tries to display the score
        every { viewModel.calculateScore(any(), any()) } returns 0

        composeTestRule.setContent {
            MastermindScreen(viewModel, {}, {}, {})
        }

        // Ensure LaunchedEffects have finished their first pass
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("guess_field_0", useUnmergedTree = true) // Allow searching children
            .performTextInput("A")

        composeTestRule.onNodeWithText("A").assertExists()
    }

    // 5. HallOfFameScreen test - Check empty list and title
    @Test
    fun hallOfFameDisplaysTitleCorrectly() {
        val viewModel = mockk<MastermindViewModel>(relaxed = true)
        // Emulate empty list
        every { viewModel.highScores.value } returns emptyList()

        composeTestRule.setContent {
            HallOfFameScreen(viewModel, onBack = {})
        }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val titleText = context.getString(R.string.hall_of_fame)

        composeTestRule.onNodeWithText(titleText).assertIsDisplayed()
    }
}
