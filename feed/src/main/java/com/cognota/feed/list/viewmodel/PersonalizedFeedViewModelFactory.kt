package com.cognota.feed.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognota.feed.list.data.PersonalizedFeedDataContract
import com.cognota.feed.list.data.SourceAndCategoryDataContract
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class PersonalizedFeedViewModelFactory @Inject constructor(
    private var repository: PersonalizedFeedDataContract.Repository,
    private var sourceAndCategoryRepository: SourceAndCategoryDataContract.Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PersonalizedFeedViewModel(repository, sourceAndCategoryRepository) as T
    }
}