package markxie.game.calendarvideo.room

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface YtInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: YtRoom)

    @Update
    fun update(data: YtRoom)

    @Delete
    fun delete(data: YtRoom)

    @Query("SELECT * FROM YtRoom WHERE yt_videoId LIKE :videoId LIMIT 1")
    fun getByVideoId(videoId: String): YtRoom

    @Query("SELECT * FROM YtRoom WHERE yt_title LIKE :title LIMIT 1")
    fun getByTitle(title: String): YtRoom

    @Query("SELECT * FROM YtRoom")
    fun getAll(): List<YtRoom>

    @Query("DELETE FROM YtRoom")
    fun nukeTable()

    @Query("SELECT * FROM YtRoom ORDER BY date DESC LIMIT:limit OFFSET :offset")
    fun getByPos(offset: Int, limit: Int): List<YtRoom>

    @Query("SELECT * FROM YtRoom ORDER BY date")
    fun getDataSource(): DataSource.Factory<Int, YtRoom>


}