package com.seo.visualexhibition.ui.template

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seo.visualexhibition.data.model.TemplateField
import com.seo.visualexhibition.databinding.ItemFieldBinding

class FieldAdapter(
    private val onEditClick: (TemplateField) -> Unit,
    private val onDeleteClick: (TemplateField) -> Unit
) : ListAdapter<TemplateField, FieldViewHolder>(object :
    DiffUtil.ItemCallback<TemplateField>() {
    override fun areItemsTheSame(
        oldItem: TemplateField,
        newItem: TemplateField
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TemplateField,
        newItem: TemplateField
    ): Boolean =
        oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder {
        val view = ItemFieldBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return FieldViewHolder(view)
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {
        val field = getItem(position)
        holder.textTemplateName?.text = field.fieldName
        holder.deleteBtn.setOnClickListener {
            onDeleteClick(field)
        }

        holder.editBtn.setOnClickListener {
            onEditClick(field)
        }
    }
}

class FieldViewHolder(binding: ItemFieldBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val textTemplateName: TextView? = binding.tvFieldName
    val deleteBtn: ImageButton = binding.btnDelete
    val editBtn: ImageButton = binding.btnEdit
}