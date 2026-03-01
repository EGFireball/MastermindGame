package com.idimi.mastermindgame.ui.presentation.navigation

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object Mastermind : Screen("game")
    object HallOfFame : Screen("hall_of_fame")
    object GameOver : Screen("game_over")
    object Success : Screen("success")
}