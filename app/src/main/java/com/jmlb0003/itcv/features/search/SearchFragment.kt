package com.jmlb0003.itcv.features.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.features.profile.ProfileDetailsFragment.Companion.getProfileDetailsBundle
import com.jmlb0003.itcv.features.search.adapter.SearchResult
import com.jmlb0003.itcv.features.search.adapter.SearchResultsAdapter
import com.jmlb0003.itcv.utils.FragmentActivity.showSoftKeyboard

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var adapter: SearchResultsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSearchView(view)
        initSearchResultsView(view)
    }

    private fun initSearchView(rootView: View) {
        rootView.findViewById<SearchView>(R.id.search_view)?.apply {
            requireActivity().showSoftKeyboard(this)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
//                    // TODO to be properly implemented
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // TODO to be properly implemented
                    adapter?.setSearchResults(newText?.split("")?.filter { it.isNotBlank() }?.map {
                        SearchResult(profileName = it, isFavorite = newText.length % 2 == 0)
                    } ?: emptyList())
                    return true
                }
            })
        }
    }

    private fun initSearchResultsView(rootView: View) {
        rootView.findViewById<RecyclerView>(R.id.search_results_list)?.let { recyclerView ->
            recyclerView.adapter = SearchResultsAdapter(
                onResultClicked = {
                    // TODO Pass the clicked result to open the details screen
                    view?.findNavController()?.navigate(
                        R.id.navigation_search_to_details,
                        getProfileDetailsBundle(
                            profileName = it.profileName
                        )
                    )
                }
            ).also { adapter = it }
        }
    }
}
