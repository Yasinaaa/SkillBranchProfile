package ru.skillbranch.skillarticles.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_chose_category_dialog.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.viewmodels.articles.ArticlesViewModel


class ChoseCategoryDialog : DialogFragment() {
    private val viewModel: ArticlesViewModel by activityViewModels()
    private val args: ChoseCategoryDialogArgs by navArgs()
    private val categories by lazy { args.categories }
    private val selected = mutableSetOf<String>()
    private var listView: View? = null

    private val categoriesAdapter = CategoriesAdapter { categoryId: String, isChecked: Boolean ->
        if (isChecked) selected.add(categoryId)
        else selected.remove(categoryId)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        selected.clear()
        selected.addAll(savedInstanceState?.getStringArray("checked") ?: args.selectedCategories)
        val categoryItems = args.categories.map { it.toItem(selected.contains(it.categoryId)) }
        categoriesAdapter.submitList(
            categoryItems
        )

        listView =
            layoutInflater.inflate(R.layout.fragment_chose_category_dialog, null) as RecyclerView

        with(listView as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = categoriesAdapter
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Chose category")
            .setPositiveButton("Apply") { _, _ ->
                viewModel.applyCategories(selected.toList())
            }
            .setNegativeButton("Reset") { _, _ ->
                viewModel.applyCategories(emptyList())
            }
            .setView(listView)
            .create()
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val saved = savedInstanceState?.getStringArray("checked")
        if (saved != null) {
            selected.clear()
            selected.addAll(saved)
        }
        val categoryItems = args.categories.map { it.toItem(selected.contains(it.categoryId)) }
        categoriesAdapter.submitList(categoryItems)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArray("checked", selected.toTypedArray())
        super.onSaveInstanceState(outState)
    }


}