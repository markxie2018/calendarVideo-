package markxie.game.calendarvideo.feature.paging_list

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import markxie.game.calendarvideo.R
import markxie.game.calendarvideo.extension.inflateLayout
import markxie.game.calendarvideo.model.Info


class VideoListAdapter(private val myClick: (Info?, Int) -> Unit) :
    PagedListAdapter<Info, RecyclerView.ViewHolder>(INFO_COMPARATOR) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListViewHolder) {
            holder.bind(getItem(position))
            holder.itemView.setOnClickListener { myClick(getItem(position), position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder(parent.context.inflateLayout(R.layout.item_list))
    }

    companion object {
        private val INFO_COMPARATOR = object : DiffUtil.ItemCallback<Info>() {

            override fun areItemsTheSame(oldItem: Info, newItem: Info): Boolean =
                oldItem.videoId == newItem.videoId

            override fun areContentsTheSame(oldItem: Info, newItem: Info): Boolean =
                oldItem.title == newItem.title
        }
    }
}

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var title = view.findViewById<TextView>(R.id.videoTitle)
    private var videoDate = view.findViewById<TextView>(R.id.videoDate)

    fun bind(info: Info?) {
        info?.also {
            videoDate.text = it.dateString
            title.text = it.title

        }
    }
}

