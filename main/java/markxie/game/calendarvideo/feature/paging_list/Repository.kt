package markxie.game.calendarvideo.feature.paging_list

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import markxie.game.calendarvideo.model.Info


interface Repository {

    fun post(): LiveData<Listing<Info>>
}

interface LocalAndRemoteRepository {
    fun getInfos(): PagedList<Info>
}