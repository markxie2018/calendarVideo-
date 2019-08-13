package markxie.game.calendarvideo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class YtRoom(
    @ColumnInfo(name = "yt_title")
    var title: String,
    @PrimaryKey
    @ColumnInfo(name = "yt_videoId")
    var videoId: String,
    var description: String = "",
    var date: String = ""
)