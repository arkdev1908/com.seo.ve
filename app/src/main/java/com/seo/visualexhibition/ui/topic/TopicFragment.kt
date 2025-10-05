package com.seo.visualexhibition.ui.topic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seo.visualexhibition.Application
import com.seo.visualexhibition.data.model.Topic
import com.seo.visualexhibition.databinding.FragmentTopicBinding
import com.seo.visualexhibition.databinding.ItemTopicBinding

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
class TopicFragment : Fragment() {

    private var _binding: FragmentTopicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val app = requireContext().applicationContext as Application
        val topicViewModel = ViewModelProvider(
            this,
            TopicViewModelFactory(app.topicRepository)
        )[TopicViewModel::class.java]
        _binding = FragmentTopicBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.recyclerviewTopic
        val adapter = TopicAdapter()
        recyclerView.adapter = adapter
        topicViewModel.all.observe(viewLifecycleOwner) { topics ->
            (recyclerView.adapter as TopicAdapter).submitList(topics)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TopicAdapter() :
        ListAdapter<Topic, TopicViewHolder>(object : DiffUtil.ItemCallback<Topic>() {

            override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean =
                oldItem == newItem
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
            val binding = ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TopicViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
            val topic = getItem(position)
            holder.textTopicTitle?.text = topic.topicName
            holder.textTopicContent?.text = topic.topicDescription
            holder.imageTopic?.let {
                Glide.with(holder.imageTopic.context)
                    .load(topic.imageSrc)
                    .into(it)
            }
        }
    }

    class TopicViewHolder(binding: ItemTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageTopic: ImageView? = binding.imgTopic
        val textTopicTitle: TextView? = binding.tvTopicTitle
        val textTopicContent: TextView? = binding.tvTopicContent
    }
}