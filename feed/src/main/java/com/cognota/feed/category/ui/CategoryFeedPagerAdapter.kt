package com.cognota.feed.category.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.SourceAndCategoryDTO

class CategoryFeedPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private var categories: List<CategoryDTO> = listOf()

    fun setData(categories: SourceAndCategoryDTO?) {
        categories?.let { it ->
            it.category?.let { category ->
                if (category != this.categories) {
                    this.categories = category
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun getPageTitle(position: Int): CharSequence? {
        return categories[position].name
    }


    override fun getItemCount() = categories.size

    override fun createFragment(position: Int): Fragment {
        return CategoryFeedFragment.newInstance(categories[position].code)
    }

}