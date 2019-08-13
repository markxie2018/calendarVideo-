package markxie.game.calendarvideo.feature.paging_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list.*
import markxie.game.calendarvideo.R
import markxie.game.calendarvideo.extension.debug
import markxie.game.calendarvideo.extension.toast
import org.koin.android.viewmodel.ext.android.viewModel

class ListActivity : AppCompatActivity() {

    private val listViewModel: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = VideoListAdapter { info, pos ->
            info?.run {
                toast(info.title)
            }
        }

        initLiveData()
    }

    private fun initLiveData() {

        listViewModel.videoList.observe(this, Observer {
            it?.apply {
                (rv.adapter as VideoListAdapter).submitList(it)
            }
        })

        listViewModel.networkState.observe(this, Observer {
            it?.run {
                debug(toString())

                when (status) {
                    Status.RUNNING -> {
                        toast("Loading")
                    }
                    Status.SUCCESS -> {
                        toast("Loaded")
                    }
                    Status.FAILED -> {
                        toast("error")
                    }
                }
            }
        })
    }
}