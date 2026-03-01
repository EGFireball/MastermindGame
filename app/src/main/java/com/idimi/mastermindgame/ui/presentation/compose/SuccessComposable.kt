package com.idimi.mastermindgame.ui.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.idimi.mastermindgame.R
import com.idimi.mastermindgame.ui.presentation.MastermindViewModel

@Composable
fun SuccessScreen(
    viewModel: MastermindViewModel,
    newGame: () -> Unit,
    onMenu: () -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.triggerVictoryEvent()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.victory),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondaryContainer,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = newGame,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(60.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                stringResource(R.string.new_game),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onMenu,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(60.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                stringResource(R.string.back_to_menu),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}