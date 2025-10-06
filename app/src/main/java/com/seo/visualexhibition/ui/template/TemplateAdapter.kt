package com.seo.visualexhibition.ui.template

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seo.visualexhibition.data.model.Template
import com.seo.visualexhibition.data.model.TemplateField
import com.seo.visualexhibition.databinding.ItemTemplateBinding

class TemplateAdapter(
    private val onTemplateDeleteClick: (Template) -> Unit,
    private val onTemplateEditClick: (Template) -> Unit,
    private val getFields: (Template) -> LiveData<List<TemplateField>>,
    private val onFieldAddClick: (Template) -> Unit,
    private val onFieldDeleteClick: (TemplateField) -> Unit,
    private val onFieldEditClick: (TemplateField) -> Unit
) :
    ListAdapter<Template, TemplateViewHolder>(object : DiffUtil.ItemCallback<Template>() {
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
    ): TemplateViewHolder {
        val binding = ItemTemplateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return TemplateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TemplateViewHolder,
        position: Int
    ) {
        val template = getItem(position)
        holder.textTemplateName?.text = template.templateName
        holder.rvField?.adapter = FieldAdapter(
            onDeleteClick = { field ->
                onFieldDeleteClick(field)
            },
            onEditClick = { field ->
                onFieldEditClick(field)
            }
        )
        getFields(template).observeForever { fields ->
            (holder.rvField?.adapter as FieldAdapter).submitList(fields)
        }
        holder.btnDelete.setOnClickListener {
            onTemplateDeleteClick(template)
        }
        holder.btnEdit.setOnClickListener {
            onTemplateEditClick(template)
        }
        holder.btnAdd.setOnClickListener {
            onFieldAddClick(template)
        }
    }
}

class TemplateViewHolder(binding: ItemTemplateBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val textTemplateName: TextView? = binding.tvTemplateName
    val rvField: RecyclerView? = binding.rvFields
    val btnDelete: ImageButton = binding.btnDelete
    val btnEdit: ImageButton = binding.btnEdit
    val btnAdd: ImageButton = binding.btnAdd
}