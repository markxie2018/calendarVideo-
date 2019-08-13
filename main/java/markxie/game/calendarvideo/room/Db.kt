package markxie.game.calendarvideo.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import markxie.game.calendarvideo.App

//@TypeConverters(DateTypeConverter::class)

@Database(entities = [YtRoom::class], version = 1)
abstract class Db : RoomDatabase() {

    abstract fun ytInfoDao(): YtInfoDao

    companion object {
        @JvmStatic
        fun getInstance(): Db {
            return SingletonHolder.mInstance
        }
    }

    private object SingletonHolder {
        //靜態內部類
        val mInstance = Room.databaseBuilder(
            App.instance,
            Db::class.java, "myDB"
        ).build()
    }
}

