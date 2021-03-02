package ru.skillbranch.skillarticles.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category_dialog.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.viewmodels.articles.ArticlesViewModel

class ChoseCategoryDialog : DialogFragment() {
    companion object{
        const val CHOOSE_CATEGORY_KEY = "CHOOSE_CATEGORY_KEY"
        const val SELECTED_CATEGORIES = "SELECTED_CATEGORIES"
    }
    private val selected = mutableSetOf<String>()
    private val args: ChoseCategoryDialogArgs by navArgs()

    private val categoryAdapter = CategoryAdapter { categoryId: String, isChecked: Boolean ->
        if (isChecked) selected.add(categoryId)
        else selected.remove(categoryId)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        selected.clear()
        selected.addAll(
            savedInstanceState?.getStringArray("checked") ?: args.selectedCategories
        )
        val categoryItems = args.categories.map {
            it.toItem(selected.contains(it.categoryId)) }

        categoryAdapter.submitList(categoryItems)

        //inflate list
        val listView =
            layoutInflater.inflate(R.layout.fragment_choose_category_dialog, null) as RecyclerView

        //list settings
        with(listView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Chose category")
            .setPositiveButton("Apply") { _, _ ->
                setFragmentResult(CHOOSE_CATEGORY_KEY, bundleOf(SELECTED_CATEGORIES to selected.toList()))
            }
            .setNegativeButton("Reset") { _, _ ->
                setFragmentResult(CHOOSE_CATEGORY_KEY, bundleOf(SELECTED_CATEGORIES to emptyList<String>()))
            }
            .setView(listView)
            .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArray("checked", selected.toTypedArray())
        super.onSaveInstanceState(outState)
    }

}




class CategoryDiffCallback() : DiffUtil.ItemCallback<CategoryDataItem>() {
    override fun areItemsTheSame(oldItem: CategoryDataItem, newItem: CategoryDataItem): Boolean =
        oldItem.categoryId == newItem.categoryId

    override fun areContentsTheSame(oldItem: CategoryDataItem, newItem: CategoryDataItem): Boolean =
        oldItem == newItem
}

data class CategoryDataItem(
    val categoryId: String,
    val icon: String,
    val title: String,
    val articlesCount: Int = 0,
    val isChecked: Boolean = false
)
