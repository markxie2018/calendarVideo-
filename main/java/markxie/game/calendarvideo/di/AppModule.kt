package markxie.game.calendarvideo.di

import markxie.game.calendarvideo.feature.calendar.CalendarViewModel
import markxie.game.calendarvideo.feature.calendar.CalendarRepository
import markxie.game.calendarvideo.feature.paging_list.DbListDataSourceFactory
import markxie.game.calendarvideo.feature.paging_list.ListViewModel
import markxie.game.calendarvideo.feature.paging_list.LocalAndRemoteRepositoryImpl
import markxie.game.calendarvideo.feature.paging_list.Repository
import markxie.game.calendarvideo.room.Db
import markxie.game.calendarvideo.room.YtInfoDao
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { CalendarRepository(get()) }
    factory { DbListDataSourceFactory() }
    factory { LocalAndRemoteRepositoryImpl(get(), get()) }
}

val appViewModelModule = module {
    viewModel { CalendarViewModel(get()) }
    viewModel { ListViewModel(get()) }
}

val localModule = module {
    single { Db.getInstance() }
    single { get<Db>().ytInfoDao() }
}