package com.jmlb0003.itcv.features.home

sealed class HomeViewStateList {
    class Ready(val label: String) : HomeViewStateList()
    object Hidden : HomeViewStateList()
    class Error(val errorState: HomeViewErrorState) : HomeViewStateList()
}
