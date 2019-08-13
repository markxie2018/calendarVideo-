package markxie.game.calendarvideo.extension

import android.util.Log

/**
 * 在任何位置可以傳入當下的class，省去寫 class TAG
 */
inline fun <reified T> T.debug(log: Any) {
    Log.e(T::class.simpleName, log.toString())
}