package markxie.game.calendarvideo.room

import com.prolificinteractive.materialcalendarview.CalendarDay
import markxie.game.calendarvideo.model.Info
import java.text.SimpleDateFormat
import java.util.*

class DbDataMApper {

    fun convertDbDataToModel(list: List<YtRoom>): List<Info> =
        list.map { Info(convertDateToCalendarDay(it.date), it.videoId, it.title) }

    fun convertDbDataToModelForList(list: List<YtRoom>): List<Info> =
        list.map {
            Info(
                convertDateToCalendarDay(
                    date = it.date
                ),
                videoId = it.videoId,
                title = it.title,
                dateString = convertDateFormat(it.date)
            )
        }


    //yt date format = 2019-05-03T02:11:41.000Z
    private fun convertDateToCalendarDay(date: String): CalendarDay {

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val calendar = GregorianCalendar()
        calendar.time = formatter.parse(date)

        return CalendarDay.from(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,//0~11
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun convertDateFormat(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val calendar = GregorianCalendar()
        calendar.time = formatter.parse(date)

        return "${calendar.get(Calendar.YEAR)}.${calendar.get(Calendar.MONTH) + 1}.${calendar.get(Calendar.DAY_OF_MONTH)}"
    }
}