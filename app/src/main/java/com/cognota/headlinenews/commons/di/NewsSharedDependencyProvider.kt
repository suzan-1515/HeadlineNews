package com.cognota.headlinenews.commons.di

import com.cognota.core.di.CoreComponent
import com.cognota.headlinenews.details.di.DaggerDetailsComponent
import com.cognota.headlinenews.details.di.DetailsComponent
import com.cognota.headlinenews.list.di.DaggerListComponent
import com.cognota.headlinenews.list.di.ListComponent
import javax.inject.Singleton

@Singleton
object NewsSharedDependencyProvider {
    private var listComponent: ListComponent? = null
    private var detailsComponent: DetailsComponent? = null

    fun listComponent(coreComponent: CoreComponent): ListComponent {
        if (listComponent == null)
            listComponent =
                DaggerListComponent.factory().create(coreComponent)
        return listComponent as ListComponent
    }

    fun destroyListComponent() {
        listComponent = null
    }

    fun detailsComponent(coreComponent: CoreComponent): DetailsComponent {
        if (detailsComponent == null)
            detailsComponent =
                DaggerDetailsComponent.builder().listComponent(listComponent(coreComponent)).build()
        return detailsComponent as DetailsComponent
    }

    fun destroyDetailsComponent() {
        detailsComponent = null
    }
}