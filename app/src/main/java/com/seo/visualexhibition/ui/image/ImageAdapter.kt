package com.seo.visualexhibition.ui.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.seo.visualexhibition.R
import com.seo.visualexhibition.data.model.DisplayImage
import com.seo.visualexhibition.databinding.ItemImageBinding

class ImageAdapter() :
    ListAdapter<DisplayImage, ImageViewHolder>(object : DiffUtil.ItemCallback<DisplayImage>() {
        override fun areItemsTheSame(
            oldItem: DisplayImage,
            newItem: DisplayImage
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DisplayImage,
            newItem: DisplayImage
        ): Boolean =
            oldItem == newItem

    }) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int
    ) {
        val image = getItem(position)
        Glide.with(holder.imageView.context)
            .load(image.imageSrc)
            .placeholder(R.drawable.ic_gallery_black_24dp)
            .error(R.drawable.ic_camera_black_24dp)
            .fitCenter()
            .into(holder.imageView)
    }
}

class ImageViewHolder(binding: ItemImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val imageView = binding.imageView
}