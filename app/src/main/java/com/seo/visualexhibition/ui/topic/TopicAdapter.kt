package com.seo.visualexhibition.ui.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seo.visualexhibition.data.model.Topic
import com.seo.visualexhibition.databinding.ItemTopicBinding

class TopicAdapter(
    private val onDeleteClick: (Topic) -> Unit,
    private val onEditClick: (Topic) -> Unit
) : ListAdapter<Topic, TopicViewHolder>(object : DiffUtil.ItemCallback<Topic>() {

    override fun areItemsTheSame(
        oldItem: Topic,
        newItem: Topic
    ): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Topic,
        newItem: Topic
    ): Boolean =
        oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding =
            ItemTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = getItem(position)
        holder.textTopicTitle?.text = topic.topicName
        holder.textTopicContent?.text = topic.topicDescription

        holder.deleteBtn.setOnClickListener {
            onDeleteClick(topic)
        }
        holder.editBtn.setOnClickListener {
            onEditClick(topic)
        }
    }
}

class TopicViewHolder(binding: ItemTopicBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val imageTopic: ImageView? = binding.imgTopic
    val textTopicTitle: TextView? = binding.tvTopicTitle
    val textTopicContent: TextView? = binding.tvTopicContent
    val deleteBtn: ImageButton = binding.btnDelete
    val editBtn: ImageButton = binding.btnEdit
}