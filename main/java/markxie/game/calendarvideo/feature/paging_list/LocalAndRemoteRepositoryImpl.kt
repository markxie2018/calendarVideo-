package markxie.game.calendarvideo.feature.paging_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import markxie.game.calendarvideo.model.Info
import markxie.game.calendarvideo.room.YtInfoDao


class LocalAndRemoteRepositoryImpl(
    private val sourceFactory: DbListDataSourceFactory,
    private val ytInfoDao: YtInfoDao
) : Repository {


    override fun post(): LiveData<Listing<Info>> {

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setPrefetchDistance(4)
            .build()

        val postBoundaryCallback = PostBoundaryCallback(sourceFactory, ytInfoDao)

        val liveList = LivePagedListBuilder(sourceFactory, pagedListConfig)
            .setBoundaryCallback(postBoundaryCallback)
            .build()

        val tmp = MutableLiveData<Listing<Info>>()

        tmp.value = Listing(
            infoList = liveList,
            networkState = postBoundaryCallback.networkState
        )
        return tmp
    }
}