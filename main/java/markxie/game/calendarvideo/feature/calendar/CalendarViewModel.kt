package markxie.game.calendarvideo.feature.calendar


import androidx.lifecycle.ViewModel

class CalendarViewModel(private val repo: CalendarRepository) : ViewModel() {

    var infoList = repo.all

    fun fetch() = repo.fetch()

}