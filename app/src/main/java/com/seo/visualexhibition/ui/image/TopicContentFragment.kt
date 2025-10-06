package com.seo.visualexhibition.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.seo.visualexhibition.Application
import com.seo.visualexhibition.databinding.FragmentImageTopicBinding

class TopicContentFragment : Fragment() {
    private var topicId: Long = 0
    private var _binding: FragmentImageTopicBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageViewModel: ImageViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topicId = arguments?.getLong("topic_id") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageTopicBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val app = requireContext().applicationContext as Application
        imageViewModel = ViewModelProvider(
            this,
            ImageViewModelFactory(app.imageRepository)
        )[ImageViewModel::class.java]
        val rv = binding.rvImages
        val layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        rv.layoutManager = layoutManager
        rv.adapter = ImageAdapter()
        imageViewModel.all(topicId).observe(viewLifecycleOwner) { images ->
            (rv.adapter as ImageAdapter).submitList(images)
        }

        return root
    }

    companion object {
        fun newInstance(topicId: Long) = TopicContentFragment().apply {
            arguments = Bundle().apply {
                putLong("topic_id", topicId)
            }
        }
    }
}
