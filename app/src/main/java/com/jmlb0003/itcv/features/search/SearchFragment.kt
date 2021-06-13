package com.jmlb0003.itcv.features.search

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jmlb0003.itcv.CustomApplication
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.features.search.adapter.SearchResult
import com.jmlb0003.itcv.features.search.adapter.SearchResultsAdapter
import com.jmlb0003.itcv.utils.FragmentActivity.showSoftKeyboard
import javax.inject.Inject

class SearchFragment : Fragment(R.layout.fragment_search) {

    @Inject
    lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var searchResultsAdapter: SearchResultsAdapter

    private val onViewStateChange = Observer<SearchViewStateList> {
        handleViewStateChange(it)
    }

    override fun onAttach(context: Context) {
        initSearchComponent(context)
        super.onAttach(context)
    }

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
            recyclerView.adapter = searchResultsAdapter
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
            else -> displayError()
        }
        if (state != SearchViewStateList.Loading) {
            hideLoading()
        }
        if (state != SearchViewStateList.Empty) {
            hideEmptyView()
        }
        if (state !is SearchViewStateList.ListOfResults) {
            searchResultsAdapter.setSearchResults(emptyList())
        }
        if (state != SearchViewStateList.Error) {
            hideErrorView()
        }
    }

    private fun displayResults(results: List<SearchResult>) {
        searchResultsAdapter.setSearchResults(results)
    }

    private fun displayEmptyResultsScreen() {
        view?.findViewById<View>(R.id.empty_results_view)?.visibility = View.VISIBLE
    }

    private fun hideEmptyView() {
        view?.findViewById<View>(R.id.empty_results_view)?.visibility = View.GONE
    }

    private fun displayLoading() {
        view?.findViewById<View>(R.id.search_loading)?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        view?.findViewById<View>(R.id.search_loading)?.visibility = View.GONE
    }

    private fun displayError() {
        view?.findViewById<View>(R.id.error_results_view)?.visibility = View.VISIBLE
    }

    private fun hideErrorView() {
        view?.findViewById<View>(R.id.error_results_view)?.visibility = View.GONE
    }

    private fun initSearchComponent(context: Context) {
        (context.applicationContext as CustomApplication)
            .appComponent
            .searchComponentFactory.create(
                this,
                onResultClicked = { viewModel.presenter.onResultClicked(it) }
            ).also {
                it.inject(this)
            }
    }
}
