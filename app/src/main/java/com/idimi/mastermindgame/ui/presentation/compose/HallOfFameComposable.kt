package com.idimi.mastermindgame.ui.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idimi.mastermindgame.R
import com.idimi.mastermindgame.domain.model.MastermindResult
import com.idimi.mastermindgame.ui.presentation.MastermindViewModel
import com.idimi.mastermindgame.ui.theme.Bronze
import com.idimi.mastermindgame.ui.theme.Golden
import com.idimi.mastermindgame.ui.theme.Silver
import com.idimi.mastermindgame.utils.formatTimestamp

@Composable
fun HallOfFameScreen(
    viewModel: MastermindViewModel
) {
    val highScores by viewModel.highScores.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getHighScores()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        item { Spacer(modifier = Modifier.height(20.dp)) }
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.hall_of_fame),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
        itemsIndexed(highScores) { index, result ->
            HighScoreItem(rank = index + 1, result = result)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun HighScoreItem(rank: Int, result: MastermindResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
            .border(
                width = 1.dp,
                color = if (rank <= 3) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            if (rank <= 3) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = when (rank) {
                        1 -> Golden
                        2 -> Silver
                        3 -> Bronze
                        else -> Color.White
                    },
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = rank.toString(),
                fontWeight = FontWeight.ExtraBold,
                color = if (rank <= 3) Color.Black else MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Name
        Text(
            text = result.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        // Score
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${result.score}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Black
            )
            // Time
            Text(
                text = formatTimestamp(result.time),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
