package com.cognota.feed.commons.data.local.dao

import androidx.room.*
import com.cognota.core.data.persistence.dao.BaseDao
import com.cognota.feed.commons.data.local.entity.CategoryEntity
import com.cognota.feed.commons.data.local.entity.FeedEntity
import com.cognota.feed.commons.data.local.entity.RelatedFeedEntity
import com.cognota.feed.commons.data.local.entity.SourceEntity
import com.cognota.feed.commons.data.local.relation.FeedWithRelatedEntity
import com.cognota.feed.commons.data.local.relation.FeedWithSourcesEntity

@Dao
abstract class NewsDao : BaseDao<FeedEntity>() {

    @Transaction
    @Query("SELECT * from feed where type = 'LATEST' order by update_date desc")
    abstract fun findLatestFeeds(): List<FeedWithRelatedEntity>?

    @Transaction
    @Query("SELECT * from feed where type = 'TOP' order by update_date desc")
    abstract fun findTopFeeds(): List<FeedWithSourcesEntity>?

    @Transaction
    @Query("SELECT * from feed where category_code = :category order by update_date desc")
    abstract fun findFeedsByCategory(category: String): List<FeedWithSourcesEntity>?

    @Query("SELECT * from source order by priority asc")
    abstract fun findSources(): List<SourceEntity>?

    @Query("SELECT * from source where code = :code limit 1")
    abstract fun findSourceByCode(code: String): SourceEntity?

    @Query("SELECT * from category where code = :code limit 1")
    abstract fun findCategoryByCode(code: String): CategoryEntity?

    @Query("SELECT * from category where enable = 'Y' order by priority asc")
    abstract fun findCategories(): List<CategoryEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRelatedFeeds(sources: List<RelatedFeedEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRelatedFeed(source: RelatedFeedEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSources(sources: List<SourceEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCategories(sources: List<CategoryEntity>): List<Long>

    @Query("DELETE from source")
    abstract fun deleteAllSources()

    @Query("DELETE from category")
    abstract fun deleteAllCategories()

    @Query("DELETE from feed")
    abstract fun deleteAllFeed()

    @Query("DELETE from feed where type = :type ")
    abstract fun deleteAllLatestFeed(type: String)

    @Query("DELETE from feed where type = type = :type ")
    abstract fun deleteAllTopFeed(type: String)

    @Query("DELETE from feed where category_code = :category")
    abstract fun deleteAllFeedByCategory(category: String)
}