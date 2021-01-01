package com.jmlb0003.itcv.features.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.jmlb0003.itcv.R

class ProfileDetailsFragment : Fragment(R.layout.fragment_profile_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.profile_name)?.apply {
            text = "$text${getProfileDetailsArguments().profileName}"
        }
    }

    private fun getProfileDetailsArguments() = arguments?.getParcelable<ProfileDetailsArgs>(ARG_PROFILE_DETAILS)
        ?: throw IllegalStateException("There needs to be a ProfileDetailsArgs input")

    companion object {
        private const val ARG_PROFILE_DETAILS = "Args:ProfileDetailsArgs"

        fun getProfileDetailsBundle(profileName: String) = bundleOf(
            ARG_PROFILE_DETAILS to ProfileDetailsArgs(
                profileName = profileName
            )
        )
    }
}
