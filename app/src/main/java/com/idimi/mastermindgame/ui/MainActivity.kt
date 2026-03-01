package com.idimi.mastermindgame.ui

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.idimi.mastermindgame.R
import com.idimi.mastermindgame.ui.presentation.MastermindViewModel
import com.idimi.mastermindgame.ui.theme.MastermindGameTheme
import com.idimi.mastermindgame.ui.presentation.compose.GameOverScreen
import com.idimi.mastermindgame.ui.presentation.compose.HallOfFameScreen
import com.idimi.mastermindgame.ui.presentation.compose.MastermindScreen
import com.idimi.mastermindgame.ui.presentation.compose.MenuScreen
import com.idimi.mastermindgame.ui.presentation.compose.SuccessScreen
import com.idimi.mastermindgame.ui.presentation.navigation.Screen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MastermindViewModel by viewModel()
    private var mediaPlayer: MediaPlayer? = null
    private var victoryMedia: MediaPlayer? = null
    private var gameOverMedia: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaPlayer = MediaPlayer.create(this, R.raw.background).apply {
            isLooping = true
        }

        victoryMedia = MediaPlayer.create(this, R.raw.victory)
        gameOverMedia = MediaPlayer.create(this, R.raw.game_over)

        // Handling game events
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gameEvents.collect { event ->
                    when (event) {
                        is MastermindViewModel.GameEvent.OnVictory -> {
                            victoryMedia?.let {
                                handleMusicTransition(
                                    mediaToPlay = it,
                                    mediaToStop = mediaPlayer!!
                                )
                            }
                        }

                        MastermindViewModel.GameEvent.OnGameOver -> {
                            gameOverMedia?.let {
                                handleMusicTransition(
                                    mediaToPlay = it,
                                    mediaToStop = mediaPlayer!!
                                )
                            }
                        }
                    }
                }
            }
        }

        enableEdgeToEdge()
        setContent {

            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

            val backgroundImageRes = if (isSystemInDarkTheme()) {
                R.drawable.mastermind_night
            } else {
                R.drawable.mastermind_day
            }

            MastermindGameTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = backgroundImageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding(),
                        containerColor = Color.Transparent,
                    ) { innerPadding ->
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        victoryMedia?.release()
        victoryMedia = null
        gameOverMedia?.release()
        gameOverMedia = null
    }

    private fun handleMusicTransition(mediaToPlay: MediaPlayer, mediaToStop: MediaPlayer) {
        mediaToStop.pause()

        mediaToPlay.apply {
            seekTo(0)
            start()

            setOnCompletionListener {
                mediaToStop.start()
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MastermindViewModel
) {

    val activity = LocalContext.current

    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Menu.route
    ) {
        composable(Screen.Menu.route) {
            MenuScreen(
                onNewGame = { navController.navigate(Screen.Mastermind.route) },
                onShowHallOfFame = { navController.navigate(Screen.HallOfFame.route) },
                onQuit = { (activity as MainActivity).finish() }
            )
        }

        composable(Screen.Mastermind.route) {
            MastermindScreen(
                viewModel = viewModel,
                onGameOver = { navController.navigate(Screen.GameOver.route) {
                    popUpTo(Screen.Menu.route)
                }},
                onSuccess = { navController.navigate(Screen.Success.route) {
                    popUpTo(Screen.Menu.route)
                }}
            )
        }

        composable(Screen.HallOfFame.route) {
            HallOfFameScreen(
                viewModel = viewModel
            )
        }

        composable(Screen.Success.route) {
            SuccessScreen(
                viewModel = viewModel,
                newGame = {
                    navController.navigate(Screen.Mastermind.route) {
                        popUpTo(Screen.Menu.route)
                    }
                },
                onMenu = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Menu.route)
                    }
                }
            )
        }

        composable(Screen.GameOver.route) {
            GameOverScreen(
                viewModel = viewModel,
                onRetry = {
                    navController.navigate(Screen.Mastermind.route) {
                        popUpTo(Screen.Menu.route)
                    }
                },
                onMenu = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Menu.route)
                    }
                }
            )
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context as? MainActivity ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}
