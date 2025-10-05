package com.seo.visualexhibition.ui.topic

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seo.visualexhibition.Application
import com.seo.visualexhibition.R
import com.seo.visualexhibition.data.model.Topic
import com.seo.visualexhibition.databinding.FragmentTopicBinding
import com.seo.visualexhibition.databinding.ItemTopicBinding
class TopicFragment : Fragment() {

    private var _binding: FragmentTopicBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                imgPreviewInDialog?.setImageURI(it)
            }
        }
    private var imgPreviewInDialog: ImageView? = null


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
        binding.btnAddTopic.setOnClickListener {
            showAddTopicDialog()
        }
        return root
    }

    private fun showAddTopicDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_topic, null)
        val etName = dialogView.findViewById<EditText>(R.id.etTopicName)
        val etDesc = dialogView.findViewById<EditText>(R.id.etTopicDescription)
        val btnSelectImage = dialogView.findViewById<Button>(R.id.btnSelectImage)
        imgPreviewInDialog = dialogView.findViewById(R.id.imgPreview)

        btnSelectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Thêm") { d, _ ->
                val name = etName.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                Toast.makeText(requireContext(), "Đã thêm: $name", Toast.LENGTH_SHORT).show()
                d.dismiss()
            }
            .setNegativeButton("Hủy") { d, _ ->
                d.dismiss()
            }
            .create()
        dialog.show()
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