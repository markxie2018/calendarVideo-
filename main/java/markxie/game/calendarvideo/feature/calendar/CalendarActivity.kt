package markxie.game.calendarvideo.feature.calendar

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.threeten.bp.LocalDate
import com.prolificinteractive.materialcalendarview.CalendarDay
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import markxie.game.calendarvideo.extension.debug
import markxie.game.calendarvideo.lib.EventDecorator
import org.threeten.bp.format.DateTimeFormatter
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import markxie.game.calendarvideo.adapter.YtCardViewPager

import markxie.game.calendarvideo.extension.inflateLayout
import markxie.game.calendarvideo.model.Info
import markxie.game.calendarvideo.room.YtInfoDao
import org.jetbrains.anko.find
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates


class CalendarActivity : AppCompatActivity(),
    OnDateSelectedListener, OnMonthChangedListener,
    OnDateLongClickListener {

    private val FORMATTER = DateTimeFormatter.ofPattern("yyyy MMM d, EEE")

    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val calendarModel: CalendarViewModel by viewModel()

    //param need to care
    private var isFullscreen = false

    private var selectText: String by Delegates.observable(
        "no selection"
    ) { _, _, new ->
        tv_info.text = new
    }

    //使用 mutableList 要 observable 必須直接給值，add or clear 不會觸發
    var viewPagerList by Delegates.observable(mutableListOf<Info>()) { _, _, newList ->
        when {
            newList.isEmpty() -> {
                tv_info.visibility = View.VISIBLE
                ll_ytContainer.visibility = View.GONE
            }
            newList.isNotEmpty() -> {
                tv_info.visibility = View.GONE
                ll_ytContainer.visibility = View.VISIBLE
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(markxie.game.calendarvideo.R.layout.activity_calendar)

        clearCatchInSupportFm(savedInstanceState)

        initView()
        initListener()
        initCalendar()
        initializeYoutubePlayer()
        initLiveData()
    }

    //clear all fragments
    private fun clearCatchInSupportFm(savedInstanceState: Bundle?) {
        for (f in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(f).commit()
        }
//        savedInstanceState?.let {
//            debug("savedInstanceState not null ")
//            //app 在後台被殺後返回 ytPlayerFragment 存有一份在supportFragmentManager 要清掉
//            for (f in supportFragmentManager.fragments) {
//                debug("remove")
//                supportFragmentManager.beginTransaction().remove(f).commit()
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        calendarModel.fetch()
    }

    private fun initLiveData() {
        debug("initLiveData")
        val eventList = mutableListOf<CalendarDay>()

        calendarModel.infoList.observe(this, Observer { list ->
            list?.run {
                list.forEach { eventList.add(it.date) }

                //添加各種裝飾
                calendarView.addDecorators(
                    EventDecorator(eventList, this@CalendarActivity)
                )

                eventList.clear()
            }
        })
    }

    private fun initView() {

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)

        supportActionBar?.title = "Test"
        selectText = "no selection"
    }

    private fun initListener() {
        calendarView.setOnDateChangedListener(this)
        calendarView.setOnDateLongClickListener(this)
        calendarView.setOnMonthChangedListener(this)

        sheetBehavior.setBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(view: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            ytPlayer?.run {
                                if (isPlaying) pause()
                            }
                        }
                        BottomSheetBehavior.STATE_DRAGGING -> {
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                        }
                        else -> {
                        }
                    }
                }

                override fun onSlide(view: View, v: Float) {

                }
            })

        vp_ytCard.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                debug("onPageSelected = $position")
                ytPlayer?.loadVideo(viewPagerList[position].videoId, 0)
            }

        })
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay, selected: Boolean
    ) {

        val videosInOneDayList = calendarModel.infoList.value?.filter { it.date == date }

        if (videosInOneDayList != null && videosInOneDayList.isNotEmpty()) {

            viewPagerList = videosInOneDayList as MutableList<Info>
            setViewPagerData(videosInOneDayList)

            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            ytPlayer?.loadVideo(viewPagerList[0].videoId, 0)

        } else {

            viewPagerList = mutableListOf()

            selectText = if (selected) FORMATTER.format(date.date) else "No Selection"

            if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
//        supportActionBar?.title = FORMATTER.format(date?.date)
    }

    override fun onDateLongClick(widget: MaterialCalendarView, date: CalendarDay) {
        val text = String.format("%s is available", FORMATTER.format(date.date))
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun initCalendar() {
        calendarView.showOtherDates = MaterialCalendarView.SHOW_ALL
        val instance: LocalDate = LocalDate.now()
        calendarView.setSelectedDate(instance)
        selectText = FORMATTER.format(instance)
    }

    private fun setViewPagerData(list: List<Info>) {
        vp_ytCard.adapter = null
        vp_ytCard.adapter = YtCardViewPager()
        val views = mutableListOf<View>()
        for (info in list) {
            val view = inflateLayout(markxie.game.calendarvideo.R.layout.item_yt_card)
            val text = view.find<TextView>(markxie.game.calendarvideo.R.id.tv_info)
            text.text = info.title
            views.add(view)
        }
        (vp_ytCard.adapter as YtCardViewPager).setViews(views)
    }


    override fun onDestroy() {
        super.onDestroy()
        debug("onDestroy")

        ytPlayer?.release()
    }


    //youtube player to play video when new video selected
    private var ytPlayer: YouTubePlayer? = null
    //youtube player fragment
    private var ytPlayerFragment: YouTubePlayerSupportFragment? = null

    /**
     * youtube native
     * initialize youtube player via Fragment and get instance of YoutubePlayer
     */
    private fun initializeYoutubePlayer() {
        debug("initializeYoutubePlayer")

        ytPlayerFragment = supportFragmentManager
            .findFragmentById(markxie.game.calendarvideo.R.id.f_yt_player) as YouTubePlayerSupportFragment?

        ytPlayerFragment?.run {

            initialize("asd", object : YouTubePlayer.OnInitializedListener {

                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    player: YouTubePlayer,
                    wasRestored: Boolean
                ) {
                    if (!wasRestored) {
                        ytPlayer = player

                        ytPlayer?.run {

                            //set the player style default
                            setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                            //設定關閉全螢幕第一次開bottom sheet 會上升到頂部...
//                            setShowFullscreenButton(false)

                            fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI

                            setOnFullscreenListener {
                                isFullscreen = it
                                debug(it)

                                requestedOrientation = if (it) {
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE or
                                            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

                                } else {
                                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                }
                            }
                        }
                    }
                }

                override fun onInitializationFailure(
                    arg0: YouTubePlayer.Provider,
                    arg1: YouTubeInitializationResult
                ) {

                    //print or show error if initialization failed
                    debug("Youtube Player View initialization failed")
                }
            })
        }
    }

    override fun onBackPressed() {
        when {
            isFullscreen -> ytPlayer?.setFullscreen(false)

            sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED ->
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            !isFullscreen -> super.onBackPressed()
        }
    }
}


