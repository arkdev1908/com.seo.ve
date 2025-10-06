package com.seo.visualexhibition.ui.image

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.seo.visualexhibition.Application
import com.seo.visualexhibition.R
import com.seo.visualexhibition.data.model.DisplayImage
import com.seo.visualexhibition.data.model.ImageField
import com.seo.visualexhibition.databinding.DialogAddImageBinding
import com.seo.visualexhibition.databinding.DialogImageTemplateBinding
import com.seo.visualexhibition.databinding.FragmentImageBinding
import com.seo.visualexhibition.ui.template.TemplateViewModel
import com.seo.visualexhibition.ui.template.TemplateViewModelFactory
import com.seo.visualexhibition.ui.topic.TopicViewModel
import com.seo.visualexhibition.ui.topic.TopicViewModelFactory
import kotlin.collections.forEachIndexed

class ImageFragment : Fragment() {
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private val templateViewModel: TemplateViewModel by activityViewModels() {
        val app = requireActivity().application as Application
        TemplateViewModelFactory(app.templateRepository)
    }
    private val topicViewModel: TopicViewModel by activityViewModels() {
        val app = requireActivity().application as Application
        TopicViewModelFactory(app.topicRepository)
    }
    private val imageViewModel: ImageViewModel by activityViewModels() {
        val app = requireActivity().application as Application
        ImageViewModelFactory(app.imageRepository)
    }

    private val pickMultipleImages =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            if (uris.isNotEmpty()) {
                imageViewModel.setSelectedImages(uris)
                showSaveDialogsSequentially(uris)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        topicViewModel.all.observe(viewLifecycleOwner) { topics ->
            val adapter = TopicPagerAdapter(this, topics)
            binding.viewPager.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = topics[position].topicName
            }.attach()

            binding.viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val currentTopic = topics[position]
                    imageViewModel.setTopicId(currentTopic.id)
                }
            })
        }
        binding.btnAdd.setOnClickListener {
            templateDialog()
        }
        return root;
    }

    private fun templateDialog() {
        val dialogImageTemplateBinding =
            DialogImageTemplateBinding.inflate(LayoutInflater.from(context))
        val rv = dialogImageTemplateBinding.rvImageTemplates
        lateinit var dialog: AlertDialog
        rv.adapter = TemplateContentAdapter(
            onItemClick = { template ->
                imageViewModel.setTemplateId(template.id)
                pickMultipleImages.launch(arrayOf("image/*"))
                dialog.dismiss()
            }
        )
        templateViewModel.all.observe(viewLifecycleOwner) { templates ->
            (rv.adapter as TemplateContentAdapter).submitList(templates)
        }
        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogImageTemplateBinding.root)
            .setNegativeButton("Hủy") { d, _ ->
                d.dismiss()
            }
            .create()
        dialog.show()
    }

    private fun showSaveDialogsSequentially(uris: List<Uri>) {
        if (uris.isEmpty()) return
        var index = 0

        fun showNext() {
            if (index >= uris.size) return

            val uri = uris[index]
            val dialogBinding = DialogAddImageBinding.inflate(LayoutInflater.from(requireContext()))
            val imageView = dialogBinding.dialogImagePreview

            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.ic_gallery_black_24dp)
                .error(R.drawable.ic_camera_black_24dp)
                .fitCenter()
                .into(imageView)

            dialogBinding.containerFields.removeAllViews()
            var firstEditText: EditText? = null
            val editTextMap = mutableMapOf<Long, EditText>()

            templateViewModel.allByTemplateId(imageViewModel.templateId.value!!)
                .observe(viewLifecycleOwner) { fields ->
                    fields.forEachIndexed { i, field ->
                        val editText = EditText(requireContext()).apply {
                            hint = "Thêm ${field.fieldName}"
                            textSize = 16f
                            setPadding(0, 8, 0, 8)
                            inputType = InputType.TYPE_CLASS_TEXT or
                                    InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                                    InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or
                                    InputType.TYPE_TEXT_VARIATION_NORMAL
                            minLines = 2
                            isSingleLine = false
                            setHorizontallyScrolling(false)
                            imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                            isFocusable = true
                            isFocusableInTouchMode = true
                            background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 8, 0, 8)
                            }
                        }
                        if (i == 0) firstEditText = editText
                        dialogBinding.containerFields.addView(editText)
                        editTextMap[field.id] = editText
                    }
                }


            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Lưu ảnh này?")
                .setView(dialogBinding.root)
                .setPositiveButton("Lưu") { d, _ ->
                    val image = DisplayImage(
                        topicId = imageViewModel.topicId.value!!,
                        templateId = imageViewModel.templateId.value!!,
                        imageName = getFileNameFromUri(uri),
                        imageSrc = uri.toString(),
                    )
                    val fieldValueList = editTextMap.map { (fieldId, editText) ->
                        ImageField(
                            fieldId = fieldId,
                            fieldValue = editText.text.toString()
                        )
                    }
                    imageViewModel.insertImageWithFields(image, fieldValueList)
                    d.dismiss()
                    index++
                    showNext()
                }
                .setNegativeButton("Bỏ qua") { d, _ ->
                    d.dismiss()
                    index++
                    showNext()
                }
                .setCancelable(false)
                .create()

            dialog.setOnShowListener {
                firstEditText?.requestFocus()
                firstEditText?.post {
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(firstEditText, InputMethodManager.SHOW_IMPLICIT)
                }
            }
            dialog.show()
        }

        showNext()
    }

    fun getFileNameFromUri(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        result = it.getString(nameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}