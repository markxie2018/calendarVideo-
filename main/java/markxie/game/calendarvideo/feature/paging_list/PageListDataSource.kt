package markxie.game.calendarvideo.feature.paging_list

import androidx.paging.PositionalDataSource
import markxie.game.calendarvideo.extension.debug
import markxie.game.calendarvideo.model.Info
import markxie.game.calendarvideo.room.Db
import markxie.game.calendarvideo.room.DbDataMApper


class PageListDataSource : PositionalDataSource<Info>() {

    private fun computeCount(): Int {
        return Db.getInstance().ytInfoDao().getAll().size
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Info>) {
        debug("LoadRange range ${params.startPosition}")
        callback.onResult(loadRangeInternal(params.startPosition, params.loadSize))
    }


    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Info>) {
        val totalCount = computeCount()


        val position = computeInitialLoadPosition(params, totalCount)
        val loadSize = computeInitialLoadSize(params, position, totalCount)
        val list = loadRangeInternal(position, loadSize)
        debug("position = $position,loadSize = $loadSize, list = ${list.size} ")
        callback.onResult(list, position, totalCount)
    }

    private fun loadRangeInternal(startPosition: Int, loadCount: Int): List<Info> {
        return DbDataMApper()
            .convertDbDataToModelForList(
                Db.getInstance()
                    .ytInfoDao()
                    .getByPos(offset = startPosition, limit = loadCount)
            )
    }
}