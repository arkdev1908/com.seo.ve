package com.seo.visualexhibition.ui.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seo.visualexhibition.data.model.Template
import com.seo.visualexhibition.databinding.ItemImageTemplateBinding

class TemplateContentAdapter(
    private val onItemClick: (Template) -> Unit
) :
    ListAdapter<Template, TemplateContentViewHolder>(object : DiffUtil.ItemCallback<Template>() {
        override fun areItemsTheSame(
            oldItem: Template,
            newItem: Template
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Template,
            newItem: Template
        ): Boolean =
            oldItem == newItem
    }) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemplateContentViewHolder {
        val binding = ItemImageTemplateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return TemplateContentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TemplateContentViewHolder,
        position: Int
    ) {
        val template = getItem(position)
        holder.tvTemplateName.text = template.templateName.trim()
        holder.root.setOnClickListener {
            onItemClick(template)
        }
    }
}

class TemplateContentViewHolder(private val binding: ItemImageTemplateBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val tvTemplateName = binding.tvTemplateName
    val root = binding.root
}