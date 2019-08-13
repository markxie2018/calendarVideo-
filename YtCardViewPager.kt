package markxie.game.calendarvideo.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class YtCardViewPager : PagerAdapter() {

    var list = mutableListOf<View>()

    fun setViews(_list: List<View>) {
        list.clear()
        list.addAll(_list)
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = `object` == view


    override fun getCount(): Int = list.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(list[position])
        return list[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        for (index in 0 until count) {
            if (`object` as View === list[index]) {
                return index
            }
        }
        return POSITION_NONE
    }









}