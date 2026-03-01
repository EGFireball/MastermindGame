package com.idimi.mastermindgame.ui.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.idimi.mastermindgame.R
import com.idimi.mastermindgame.ui.presentation.MastermindViewModel
import com.idimi.mastermindgame.ui.presentation.MastermindViewModel.GameEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MastermindScreen(
    viewModel: MastermindViewModel,
    onGameOver: () -> Unit,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val secretWord by viewModel.secretWord.collectAsStateWithLifecycle()

    var userGuess by remember { mutableStateOf(listOf("", "", "", "")) }
    var feedbackColors by remember { mutableStateOf(List(4) { Color.LightGray }) }
    val timeLeft by viewModel.timeLeft.collectAsStateWithLifecycle()

    val correctLettersCount = feedbackColors.count { it == Color(0xFF4CAF50) }
    val currentScore = viewModel.calculateScore(correctLettersCount, timeLeft)

    var showNameDialog by remember { mutableStateOf(false) }
    var playerName by remember { mutableStateOf("") }

    val focusRequesters = remember { List(4) { FocusRequester() } }

    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}"
    }

    LaunchedEffect(Unit) {
        viewModel.loadNewGame()
        viewModel.startTimer { onGameOver() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.score, currentScore),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formatTime(timeLeft),
                style = MaterialTheme.typography.displayMedium,
                color = if (timeLeft < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = secretWord.hint,
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4) { index ->
                    LetterInputField(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(feedbackColors[index])
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .align(Alignment.CenterVertically),
                        value = userGuess[index],
                        focusRequester = focusRequesters[index],
                        testTag = "guess_field_$index",
                        onValueChange = { char ->
                            if (char.length <= 1) {
                                val newList = userGuess.toMutableList()
                                newList[index] = char.uppercase()
                                userGuess = newList
                                if (char.isNotEmpty() && index < 3) focusRequesters[index + 1].requestFocus()
                            }
                        }
                    )
                    if (index < 3) Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = {
                    val guess = userGuess.joinToString("").trim().uppercase()
                    val secret = secretWord.word.trim().uppercase()

                    if (guess.length == 4) {
                        feedbackColors = viewModel.checkMastermindLogic(guess, secret)
                        if (guess == secret) {
                            viewModel.stopTimer()
                            showNameDialog = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(65.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.check),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showNameDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.save_score),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        stringResource(R.string.your_score, currentScore),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BasicTextField(
                        value = playerName,
                        onValueChange = { if (it.length <= 12) playerName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            onClick = {
                                showNameDialog = false
                                viewModel.resetTimer()
                                onSuccess()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(stringResource(R.string.skip))
                        }

                        Button(
                            onClick = {
                                if (playerName.isNotBlank()) {
                                    viewModel.saveScore(
                                        playerName = playerName,
                                        score = currentScore
                                    )
                                    showNameDialog = false
                                    viewModel.resetTimer()
                                    onSuccess()
                                }
                            },
                            enabled = playerName.isNotBlank()
                        ) {
                            Text(stringResource(R.string.save))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LetterInputField(
    modifier: Modifier,
    value: String,
    focusRequester: FocusRequester,
    testTag: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxSize()
                .testTag(testTag),
            textStyle = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            decorationBox = { innerTextField ->
                // This box centers the BasicTextField horizontally and vertically.
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    innerTextField()
                }
            }
        )
    }
}
