package ru.skillbranch.skillarticles.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_edit_profile_dialog.view.*
import ru.skillbranch.skillarticles.R


class EditProfileDialog : DialogFragment() {
    companion object {
        const val EDIT_PROFILE_KEY = "EDIT_PROFILE_KEY"
        const val EDIT_PROFILE_NAME = "EDIT_PROFILE_NAME"
        const val EDIT_PROFILE_ABOUT = "EDIT_PROFILE_ABOUT"
    }

    private val args: EditProfileDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = layoutInflater.inflate(R.layout.fragment_edit_profile_dialog, null)

        view.et_name.setText(args.name)
        view.et_about.setText(args.about)

        val adb = AlertDialog.Builder(requireContext())
            .setTitle("Edit profile")
            .setView(view)
            .setPositiveButton("Apply") { _, _ ->
                setFragmentResult(
                    EDIT_PROFILE_KEY,
                    bundleOf(EDIT_PROFILE_NAME to view.et_name.text.toString(), EDIT_PROFILE_ABOUT to view.et_about.text.toString())
                )
            }
            .setNegativeButton("Reset", null)

        return adb.create()
    }
}