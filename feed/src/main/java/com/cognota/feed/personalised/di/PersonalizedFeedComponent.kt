package com.cognota.feed.personalised.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.personalised.ui.PersonalizedFeedFragment
import dagger.Subcomponent

@FeatureScope
@Subcomponent(
    modules = [PersonalizedFeedModule::class]
)
interface PersonalizedFeedComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): PersonalizedFeedComponent
    }

    fun inject(fragment: PersonalizedFeedFragment)

}
