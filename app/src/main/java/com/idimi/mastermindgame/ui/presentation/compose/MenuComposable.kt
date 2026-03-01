package com.idimi.mastermindgame.ui.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idimi.mastermindgame.R

@Composable
fun MenuScreen(onNewGame: () -> Unit, onShowHallOfFame: () -> Unit, onQuit: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.mastermind),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(60.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(65.dp),
            shape = RoundedCornerShape(24.dp),
            onClick = onNewGame
        ) {
            Text(
                text = stringResource(R.string.new_game),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(36.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(65.dp),
            shape = RoundedCornerShape(24.dp),
            onClick = onShowHallOfFame
        ) {
            Text(
                text = stringResource(R.string.hall_of_fame),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.height(36.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(65.dp),
            shape = RoundedCornerShape(24.dp),
            onClick = onQuit,
            colors =
                ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        ) {
            Text(
                text = stringResource(R.string.quit),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
