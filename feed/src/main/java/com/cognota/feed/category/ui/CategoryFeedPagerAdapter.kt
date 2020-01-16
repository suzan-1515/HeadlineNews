package com.cognota.feed.category.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.SourceAndCategoryDTO

class CategoryFeedPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var categories: List<CategoryDTO> = listOf()

    fun setData(categories: SourceAndCategoryDTO?) {
        categories?.let { it ->
            it.category?.let { category ->
                this.categories = category
                notifyDataSetChanged()
            }
        }
    }

    override fun getItem(position: Int): Fragment {
        return CategoryFeedFragment.newInstance(categories[position].code)
    }

    override fun getCount(): Int = categories.size

    override fun getPageTitle(position: Int): CharSequence? {
        return categories[position].name
    }

}