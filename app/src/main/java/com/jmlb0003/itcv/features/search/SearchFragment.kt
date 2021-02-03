package com.jmlb0003.itcv.features.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.features.search.adapter.SearchResult
import com.jmlb0003.itcv.features.search.adapter.SearchResultsAdapter
import com.jmlb0003.itcv.utils.FragmentActivity.showSoftKeyboard

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val onViewStateChange = Observer<SearchViewStateList> {
        handleViewStateChange(it)
    }
    private val viewModel by viewModels<SearchViewModel> { getSearchViewModelFactory(this) }
    private var adapter: SearchResultsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSearchView(view)
        initSearchResultsView(view)
        initViewStateObservers()
    }

    private fun initSearchView(rootView: View) {
        rootView.findViewById<SearchView>(R.id.search_view)?.apply {
            requireActivity().showSoftKeyboard(this)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.presenter.onSubmitSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.presenter.onSearchTextChange(newText)
                    return true
                }
            })
        }
    }

    private fun initSearchResultsView(rootView: View) {
        rootView.findViewById<RecyclerView>(R.id.search_results_list)?.let { recyclerView ->
            recyclerView.adapter = SearchResultsAdapter(
                onResultClicked = { viewModel.presenter.onResultClicked(it) }
            ).also { adapter = it }
        }
    }

    private fun initViewStateObservers() {
        viewModel.nonNullViewState.viewState.observe(viewLifecycleOwner, onViewStateChange)
    }

    private fun handleViewStateChange(state: SearchViewStateList?) {
        when (state) {
            SearchViewStateList.Loading -> displayLoading()
            SearchViewStateList.Empty -> displayEmptyResultsScreen()
            is SearchViewStateList.ListOfResults -> displayResults(state.results)
            else -> displayError(state)
        }
        if (state != SearchViewStateList.Loading) {
            hideLoading()
        }
    }

    private fun displayResults(results: List<SearchResult>) {
        adapter?.setSearchResults(results)
    }

    private fun displayEmptyResultsScreen() {
//        TODO("Not yet implemented")
    }

    private fun displayLoading() {
//        TODO("Not yet implemented")
        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
//        TODO("Not yet implemented")
    }

    private fun displayError(state: SearchViewStateList?) {
//        TODO("Not yet implemented")
        Toast.makeText(requireContext(), "Error: $state", Toast.LENGTH_SHORT).show()
    }
}
