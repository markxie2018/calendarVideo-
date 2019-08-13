package markxie.game.calendarvideo.lib

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import markxie.game.calendarvideo.R

class EventDecorator(dates: Collection<CalendarDay>, context: Context) : DayViewDecorator {

    private val dates: HashSet<CalendarDay> = HashSet(dates)
    private val video1: Drawable? = ContextCompat.getDrawable(context, R.mipmap.ic_video1)


    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {

        view.addSpan(ForegroundColorSpan(Color.WHITE))
        video1?.run {
            view.setBackgroundDrawable(video1)
        }
    }
}