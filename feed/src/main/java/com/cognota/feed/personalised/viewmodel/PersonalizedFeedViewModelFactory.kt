package com.cognota.feed.personalised.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.personalised.data.PersonalizedFeedDataContract
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