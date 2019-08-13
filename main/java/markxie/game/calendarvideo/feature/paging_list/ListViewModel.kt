package markxie.game.calendarvideo.feature.paging_list

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap


class ListViewModel(private val repo: LocalAndRemoteRepositoryImpl) : ViewModel() {

    private val result = repo.post()

    val videoList = switchMap(result) {
        it.infoList
    }

    val networkState = switchMap(result) {
        it.networkState
    }
}