package com.jmlb0003.itcv.features.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.jmlb0003.itcv.R

class InsertUserDialog : DialogFragment() {

    init {
        isCancelable = false
    }

    // region view state observers
    private val onViewStateChanged = Observer<InsertUserDialogViewStateList> {
        handleViewStateChange(it)
    }
    private val onDoneButtonStateChanged = Observer<Boolean> {
        handleDoneButtonStateChange(it)
    }
    // endregion

    private var usernameField: EditText? = null
    private var doneButton: Button? = null

    private val viewModel by viewModels<InsertUserDialogViewModel> { getInsertUserDialogViewModelFactory(this) }

    private var newUsername: MutableLiveData<String>? = null

    fun setUsernameChangeTrigger(usernameChangeObservable: MutableLiveData<String>) {
        newUsername = usernameChangeObservable
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dialog_insert_user, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initViewObservers()
        viewModel.presenter.onViewPrepared()
    }

    private fun initViews(rootView: View) {
        usernameField = rootView.findViewById<EditText>(R.id.default_username_field).apply {
            addTextChangedListener { viewModel.presenter.onTextChange(it?.toString()) }
        }

        doneButton = rootView.findViewById<MaterialButton>(R.id.insert_user_done).apply {
            setOnClickListener {
                viewModel.presenter.onDoneClicked(usernameField?.text?.toString())
            }
        }
    }

    private fun initViewObservers() {
        with(viewModel.nonNullViewState) {
            viewState.observe(viewLifecycleOwner, onViewStateChanged)
            doneButtonState.observe(viewLifecycleOwner, onDoneButtonStateChanged)
        }
    }

    private fun handleViewStateChange(newState: InsertUserDialogViewStateList) {
        when (newState) {
            is InsertUserDialogViewStateList.Leaving -> {
                newUsername?.postValue(newState.newUsername)
                dismiss()
            }
            is InsertUserDialogViewStateList.Visible -> usernameField?.setText(newState.username)
        }
    }

    private fun handleDoneButtonStateChange(isEnabled: Boolean) {
        doneButton?.isEnabled = isEnabled
    }
}
