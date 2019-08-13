package markxie.game.calendarvideo.model

import com.prolificinteractive.materialcalendarview.CalendarDay

data class Info(
    var date: CalendarDay,
    var videoId: String = "",
    var title: String = "",
    var description: String = "",
    var dateString: String = ""
)