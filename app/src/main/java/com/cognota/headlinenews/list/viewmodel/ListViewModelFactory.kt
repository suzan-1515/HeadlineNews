package com.cognota.headlinenews.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognota.headlinenews.list.data.ListDataContract
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory @Inject constructor(
    private var repository: ListDataContract.Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(repository) as T
    }
}