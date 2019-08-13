package markxie.game.calendarvideo.feature.paging_list

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T>(
    val infoList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>
)