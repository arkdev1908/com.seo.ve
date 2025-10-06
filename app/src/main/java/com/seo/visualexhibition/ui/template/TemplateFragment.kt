package com.seo.visualexhibition.ui.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seo.visualexhibition.Application
import com.seo.visualexhibition.data.model.Template
import com.seo.visualexhibition.data.model.TemplateField
import com.seo.visualexhibition.databinding.DialogAddBinding
import com.seo.visualexhibition.databinding.FragmentTemplateBinding
import com.seo.visualexhibition.databinding.ItemFieldBinding
import com.seo.visualexhibition.databinding.ItemTemplateBinding

class TemplateFragment : Fragment() {

    private var _binding: FragmentTemplateBinding? = null
    private val binding get() = _binding!!
    private lateinit var templateViewModel: TemplateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val app = requireContext().applicationContext as Application
        templateViewModel =
            ViewModelProvider(
                this,
                TemplateViewModelFactory(app.templateRepository)
            )[TemplateViewModel::class.java]
        _binding = FragmentTemplateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.rvTemplates
        recyclerView.adapter = TemplateAdapter(
            onTemplateDeleteClick = { template ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Xóa chủ đề")
                    .setMessage("Bạn có chắc muốn xóa '${template.templateName}' không?")
                    .setPositiveButton("Xóa") { _, _ ->
                        templateViewModel.delete(template)
                        templateViewModel.deleteByTemplateId(template.id)
                        Toast.makeText(
                            requireContext(),
                            "Đã xóa ${template.templateName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
            },
            onTemplateEditClick = { template ->
                templateDialog(
                    "Sửa Mẫu",
                    "Tên Mẫu",
                    template,
                    onAction = { template ->
                        templateViewModel.update(template)
                    }
                )
            },
            getFields = { template ->
                templateViewModel.allByTemplateId(template.id)
            },
            onFieldAddClick = { template ->
                fieldDialog(
                    "Thêm Trường",
                    "Tên Trường",
                    template,
                    null,
                    onAction = { field ->
                        templateViewModel.insert(field)
                    }
                )
            },
            onFieldDeleteClick = { field ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Xóa chủ đề")
                    .setMessage("Bạn có chắc muốn xóa '${field.fieldName}' không?")
                    .setPositiveButton("Xóa") { _, _ ->
                        templateViewModel.delete(field)
                        Toast.makeText(
                            requireContext(),
                            "Đã xóa ${field.fieldName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
            },
            onFieldEditClick = { field ->
                fieldDialog(
                    "Sửa Trường",
                    "Tên Trường",
                    null,
                    field,
                    onAction = { field ->
                        templateViewModel.update(field)
                    }
                )
            }
        )
        templateViewModel.all.observe(viewLifecycleOwner) { templates ->
            (recyclerView.adapter as TemplateAdapter).submitList(templates)
        }
        binding.btnAdd.setOnClickListener {
            templateDialog(
                "Thêm Mẫu",
                "Tên Mẫu",
                null,
                onAction = { template ->
                    templateViewModel.insert(template)
                })
        }
        return root
    }

    private fun templateDialog(
        titleText: String,
        contentHint: String,
        template: Template?,
        onAction: (Template) -> Unit
    ) {
        val dialogAddBinding = DialogAddBinding.inflate(LayoutInflater.from(context))
        val tvTitle = dialogAddBinding.tvTextTitle
        val etContent = dialogAddBinding.etTextContent

        tvTitle.text = titleText
        etContent.hint = contentHint
        etContent.setText(template?.templateName)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogAddBinding.root)
            .setPositiveButton("Thêm") { d, _ ->
                val template = Template(
                    id = template?.id ?: 0,
                    templateName = etContent.text.toString().trim()
                )
                onAction(template)
                Toast.makeText(
                    requireContext(),
                    "Đã thêm: ${template.templateName}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                d.dismiss()
            }
            .setNegativeButton("Hủy") { d, _ ->
                d.dismiss()
            }
            .create()
        dialog.show()
    }

    private fun fieldDialog(
        titleText: String,
        contentHint: String,
        template: Template?,
        field: TemplateField?,
        onAction: (TemplateField) -> Unit
    ) {
        val dialogAddBinding = DialogAddBinding.inflate(LayoutInflater.from(context))
        val tvTitle = dialogAddBinding.tvTextTitle
        val etContent = dialogAddBinding.etTextContent

        tvTitle.text = titleText
        etContent.hint = contentHint
        etContent.setText(field?.fieldName)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogAddBinding.root)
            .setPositiveButton("Thêm") { d, _ ->
                val field = TemplateField(
                    id = field?.id ?: 0,
                    templateId = template?.id ?: (field?.templateId ?: 0),
                    fieldName = etContent.text.toString().trim()
                )
                onAction(field)
                Toast.makeText(requireContext(), "Đã thêm: ${field.fieldName}", Toast.LENGTH_SHORT)
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