package markxie.game.calendarvideo.feature.calendar

import androidx.lifecycle.MutableLiveData
import markxie.game.calendarvideo.App
import markxie.game.calendarvideo.Const
import markxie.game.calendarvideo.data.YtDataMapper
import markxie.game.calendarvideo.data.YtRequest
import markxie.game.calendarvideo.data.YtRowData
import markxie.game.calendarvideo.delegates.PreferenceDelegate
import markxie.game.calendarvideo.extension.debug
import markxie.game.calendarvideo.model.Info
import markxie.game.calendarvideo.room.DbDataMApper
import markxie.game.calendarvideo.room.YtInfoDao
import markxie.game.calendarvideo.room.YtRoom
import org.jetbrains.anko.doAsync

class CalendarRepository(private val ytInfoDao: YtInfoDao) {

    val all: MutableLiveData<List<Info>> = MutableLiveData()
    private val tmpList = mutableListOf<Info>()

    private var lastTotal by PreferenceDelegate(App.instance, 0, Const.PREF_VIDEO_TOTAL_NUMBER)

    private var nextPage: String = ""

    fun fetch() {
        doAsync {
            fetchFromNetwork()
        }
    }

    /**
     * 不使用 Db 有無資料來判斷要不要更新資料
     * 使用影片總數 total 來判斷
     *

     * 每次進 app 抓取前 50 筆資料
     * 判斷 total 是否與 lastTotal 相等
     *
     * 相等則
     * 更新抓取的 50 筆
     *
     * 不相等則
     * 差50筆內   更新抓取的 50 筆
     *
     * 差 50 筆以上 全部更新
     */
    private fun fetchFromNetwork() {
        debug("fetch")

        //test
//        loadFromDb()
//        return

        val data = YtRequest().excute(nextPage)
        tmpList.addAll(YtDataMapper().convertFromRowData(data)) //50
        saveData(data)

        val total = data.pageInfo.totalResults
        val pageCount = data.pageInfo.resultsPerPage
        nextPage = data.nextPageToken ?: ""
        debug("total = $total lastTotal = $lastTotal  pageCount = $pageCount")

        when {
            total == lastTotal || total - lastTotal <= 50 -> {
                loadFromDb()
            }
            else -> {
                //全下
                fetchMore(pageCount, total)
                debug(log = "tmpList = ${tmpList.size}")
                all.postValue(tmpList)
            }
        }
        lastTotal = total
    }

    private fun fetchMore(pageCount: Int, total: Int) {
        if (nextPage.isNotEmpty()) {
            for (c in pageCount..total step pageCount) {

                if (nextPage.isNotEmpty()) {
                    val d = YtRequest().excute(nextPage)
                    nextPage = d.nextPageToken ?: ""

                    tmpList.addAll(YtDataMapper().convertFromRowData(d))
                    saveData(d)
                }
            }
        }
    }

    //TODO 檢查資料是否最新的條件為何？
    private fun shouldFetch(data: List<YtRoom>): Boolean {
        if (data.size < 100) {
            //全下載
            return true
        }
        //更新
        return false
    }

    private fun loadFromDb() {
        val data = ytInfoDao.getAll()
        debug(log = "data = ${data.size}")
        all.postValue(DbDataMApper().convertDbDataToModel(data))
    }


    private fun saveData(data: YtRowData) {
        data.items.map {
            ytInfoDao.insert(
                YtRoom(it.snippet.title, it.snippet.resourceId.id, date = it.snippet.date)
            )
        }
    }
}