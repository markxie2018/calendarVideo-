package markxie.game.calendarvideo.feature.paging_list

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import markxie.game.calendarvideo.model.Info


class DbListDataSourceFactory : DataSource.Factory<Int, Info>() {

    val sourceLiveData = MutableLiveData<PageListDataSource>()

    override fun create(): DataSource<Int, Info> {
        val source = PageListDataSource()
        sourceLiveData.postValue(source)
        return source
    }
}