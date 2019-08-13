package markxie.game.calendarvideo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import markxie.game.calendarvideo.extension.startActivity
import markxie.game.calendarvideo.feature.calendar.CalendarActivity
import markxie.game.calendarvideo.feature.paging_list.ListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()
    }

    fun initListener() {
        b_calendar.setOnClickListener { startActivity<CalendarActivity>() }
        b_list.setOnClickListener { startActivity<ListActivity>() }
    }
}
