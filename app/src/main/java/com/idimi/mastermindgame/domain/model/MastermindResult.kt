package com.idimi.mastermindgame.domain.model

data class MastermindResult(
    val name: String,
    val score: Int,
    val time: Long = System.currentTimeMillis()
)