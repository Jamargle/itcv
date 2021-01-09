package com.jmlb0003.itcv.features.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jmlb0003.itcv.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()

    private var textView: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
    }

    private fun initViews(rootView: View) {
        textView = rootView.findViewById(R.id.username)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView?.text = it
        })

        rootView.findViewById<FloatingActionButton>(R.id.home_fab)?.let {
            it.setOnClickListener {
                findNavController().navigate(R.id.navigation_home_to_search_by_fab)

                Toast.makeText(requireContext(), "Display search!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
