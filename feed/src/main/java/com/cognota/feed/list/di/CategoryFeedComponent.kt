package com.cognota.feed.list.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.list.ui.CategoriesFeedFragment
import com.cognota.feed.list.ui.CategoryFeedFragment
import dagger.Subcomponent

@FeatureScope
@Subcomponent(
    modules = [CategoryFeedModule::class]
)
interface CategoryFeedComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): CategoryFeedComponent
    }

    fun inject(categoriesFeedFragment: CategoriesFeedFragment)
    fun inject(categoryFeedFragment: CategoryFeedFragment)

}
