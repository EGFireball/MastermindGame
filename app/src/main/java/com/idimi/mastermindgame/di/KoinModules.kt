package com.idimi.mastermindgame.di

import androidx.room.Room
import com.idimi.mastermindgame.data.room.MastermindDB
import com.idimi.mastermindgame.domain.repository.MastermindRepo
import com.idimi.mastermindgame.data.repository.MastermindRepoImpl
import com.idimi.mastermindgame.domain.usecases.CheckMastermindUseCase
import com.idimi.mastermindgame.domain.usecases.GetHallOfFameDataUseCase
import com.idimi.mastermindgame.domain.usecases.GetSecretWordUseCase
import com.idimi.mastermindgame.domain.usecases.SaveResultsUseCase
import com.idimi.mastermindgame.ui.presentation.MastermindViewModel
import com.idimi.mastermindgame.utils.Constants
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Dispatchers.IO }
    single<MastermindRepo> { MastermindRepoImpl(get(), get(), get()) }

    factory { GetSecretWordUseCase(get()) }
    factory { SaveResultsUseCase(get()) }
    factory { GetHallOfFameDataUseCase(get()) }
    factory { CheckMastermindUseCase() }

    viewModel {
        MastermindViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            MastermindDB::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<MastermindDB>().gameDao() }
    single { get<MastermindDB>().wordDao() }
}
