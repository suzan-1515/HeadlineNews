package com.cognota.headlinenews.details.viewmodel

import com.cognota.core.ui.BaseViewModel
import com.cognota.headlinenews.commons.di.NewsSharedDependencyProvider

class DetailsViewModel : BaseViewModel() {

    override fun onCleared() {
        NewsSharedDependencyProvider.destroyDetailsComponent()
        super.onCleared()
    }

}