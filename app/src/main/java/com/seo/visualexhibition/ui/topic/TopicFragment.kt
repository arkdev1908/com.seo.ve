package com.seo.visualexhibition.ui.topic

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
    private lateinit var topicViewModel: TopicViewModel

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
        topicViewModel = ViewModelProvider(
            this,
            TopicViewModelFactory(app.topicRepository)
        )[TopicViewModel::class.java]
        _binding = FragmentTopicBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerviewTopic
        val adapter = TopicAdapter(
            onDeleteClick = { topic ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Xóa chủ đề")
                    .setMessage("Bạn có chắc muốn xóa '${topic.topicName}' không?")
                    .setPositiveButton("Xóa") { _, _ ->
                        topicViewModel.delete(topic)
                        Toast.makeText(
                            requireContext(),
                            "Đã xóa ${topic.topicName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
            },
            onEditClick = { topic ->
                showAddTopicDialog(topic)
            }
        )
        recyclerView.adapter = adapter
        topicViewModel.all.observe(viewLifecycleOwner) { topics ->
            (recyclerView.adapter as TopicAdapter).submitList(topics)
        }
        binding.btnAddTopic.setOnClickListener {
            showAddTopicDialog(null)
        }
        return root
    }

    private fun showAddTopicDialog(topic: Topic?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_topic, null)
        val etName = dialogView.findViewById<EditText>(R.id.etTopicName)
        val etDesc = dialogView.findViewById<EditText>(R.id.etTopicDescription)
        val btnSelectImage = dialogView.findViewById<Button>(R.id.btnSelectImage)
        imgPreviewInDialog = dialogView.findViewById(R.id.imgPreview)

        if (topic != null) {
            etName.setText(topic.topicName)
            etDesc.setText(topic.topicDescription)
        }

        btnSelectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Thêm") { d, _ ->
                val topic = Topic(
                    id = topic?.id ?: 0,
                    topicName = etName.text.toString().trim(),
                    topicDescription = etDesc.text.toString().trim().ifBlank { "" },
                    imageSrc = "",
                )
                topicViewModel.insert(topic)
                Toast.makeText(requireContext(), "Đã thêm: ${topic.topicName}", Toast.LENGTH_SHORT)
                    .show()
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
}