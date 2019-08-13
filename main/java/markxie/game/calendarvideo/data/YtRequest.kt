package markxie.game.calendarvideo.data

import com.google.gson.Gson
import markxie.game.calendarvideo.App
import markxie.game.calendarvideo.R
import markxie.game.calendarvideo.extension.debug

class YtRequest {

    companion object {
        private const val API_URL = "https://www.googleapis.com/youtube/v3/playlistItems?"
        private const val API_PARAM = "part=%s&playlistId=%s&key=%s&maxResults=%s&pageToken="
        private const val PART = "snippet"
        private const val PLAY_LIST_ID = "PL_mdITDph7YjdClTzHJJwBv07ws_W7w9Z"
        private const val MAX_RESULTS = "50"
    }

    @Throws(Exception::class)
    fun excute(nextPage: String = ""): YtRowData {
        debug("excute")

        val forecastJsonStr = java.net.URL(
            String.format(
                API_URL + API_PARAM + nextPage,
                PART,
                PLAY_LIST_ID,
                App.instance.resources.getString(R.string.youtube_api_key),
                MAX_RESULTS
            )
        ).readText()
        return Gson().fromJson(forecastJsonStr, YtRowData::class.java)
    }
}