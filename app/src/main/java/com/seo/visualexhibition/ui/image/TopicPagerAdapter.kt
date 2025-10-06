package com.seo.visualexhibition.ui.image

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.seo.visualexhibition.data.model.Topic

class TopicPagerAdapter(
    fragment: Fragment,
    private val topics: List<Topic>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = topics.size

    override fun createFragment(position: Int): Fragment {
        val topic = topics[position]
        return TopicContentFragment.newInstance(topic.id)
    }
}
