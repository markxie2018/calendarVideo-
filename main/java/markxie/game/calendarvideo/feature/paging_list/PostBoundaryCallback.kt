package markxie.game.calendarvideo.feature.paging_list

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import markxie.game.calendarvideo.data.YtRequest
import markxie.game.calendarvideo.data.YtRowData
import markxie.game.calendarvideo.extension.debug
import markxie.game.calendarvideo.model.Info
import markxie.game.calendarvideo.room.Db
import markxie.game.calendarvideo.room.YtInfoDao
import markxie.game.calendarvideo.room.YtRoom
import org.jetbrains.anko.doAsync

class PostBoundaryCallback(
    private val source: DbListDataSourceFactory,
    private val ytInfoDao: YtInfoDao
) : PagedList.BoundaryCallback<Info>() {

    private var nextPage: String? = null

    val networkState = MutableLiveData<NetworkState>()


    /**
     * database empty
     */
    override fun onZeroItemsLoaded() {
//        super.onZeroItemsLoaded()
        networkState.postValue(NetworkState.LOADING)
        doAsync {
            try {
                val data = YtRequest().excute("")
                nextPage = data.nextPageToken
                debug("nextPageToken = $nextPage")
                saveData(data)
                networkState.postValue(NetworkState.LOADED)
            } catch (e: Exception) {
                debug(e.toString())
            }
        }
    }

    /**
     * database data show over
     * require to api
     */
    override fun onItemAtEndLoaded(itemAtEnd: Info) {
//        super.onItemAtEndLoaded(itemAtEnd)

        if (nextPage.isNullOrEmpty()) {
            debug("end")
            return
        }

        networkState.postValue(NetworkState.LOADING)
        doAsync {
            try {
                val data = YtRequest().excute(nextPage!!)
                nextPage = data.nextPageToken
                saveData(data)
                networkState.postValue(NetworkState.LOADED)
            } catch (e: Exception) {
                debug(e.toString())
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Info) {
        super.onItemAtFrontLoaded(itemAtFront)
    }

    private fun saveData(data: YtRowData) {
        data.items.map {
            ytInfoDao.insert(
                YtRoom(
                    it.snippet.title,
                    it.snippet.resourceId.id,
                    date = it.snippet.date
                )
            )
        }
        source.sourceLiveData.value?.run {
            invalidate()
            debug("saveData")
        }
    }
}