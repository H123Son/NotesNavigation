package com.son.notesnavigation.presentation.editnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.son.notesnavigation.domain.Note
import com.son.notesnavigation.presentation.closeSoftKeyboard
import com.son.notesnavigation.R
import kotlinx.android.synthetic.main.edit_note_fragment.*

class EditNoteFragment : Fragment() {

    private lateinit var viewModel: EditNoteViewModel

    private val args by navArgs<EditNoteFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edit_note_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EditNoteViewModel::class.java]
        viewModel.observableCurrentNote.observe(viewLifecycleOwner, Observer { currentNote ->
            currentNote?.let { initCurrentNote(currentNote) }
        })
        viewModel.observableEditStatus.observe(viewLifecycleOwner, Observer { editStatus ->
            editStatus?.let { render(editStatus) }
        })

        viewModel.initNote(args.noteId)

        setupEditNoteSubmitHandling()
    }

    private fun initCurrentNote(note: Note) {
        editNoteText.setText(note.text)
    }

    private fun render(editStatus: Boolean) {
        when (editStatus) {
            true -> {
                findNavController().popBackStack()
            }
            false -> editNoteText.error = getString(R.string.error_validating_note)
        }
    }

    private fun setupEditNoteSubmitHandling() {
        editNoteText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.editNote(args.noteId, v.text.toString())
                v.closeSoftKeyboard()
                true
            } else {
                false
            }
        }
    }
}
