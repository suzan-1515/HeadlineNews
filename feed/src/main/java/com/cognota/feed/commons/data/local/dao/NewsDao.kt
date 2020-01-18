package com.cognota.feed.commons.data.local.dao

import androidx.room.*
import com.cognota.core.data.persistence.dao.BaseDao
import com.cognota.feed.commons.data.local.entity.*
import com.cognota.feed.commons.data.local.relation.FeedWithRelatedEntity
import com.cognota.feed.commons.data.local.relation.FeedWithSourcesEntity
import com.cognota.feed.commons.data.local.relation.RelatedFeedWithSourcesEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NewsDao : BaseDao<FeedEntity>() {

    @Transaction
    @Query("SELECT * from feed where type = 'LATEST' order by update_date desc")
    abstract fun findLatestFeeds(): List<FeedWithRelatedEntity>?

    @Transaction
    @Query("SELECT * from feed where type = 'TOP' order by update_date desc")
    abstract fun findTopFeeds(): List<FeedWithSourcesEntity>?

    @Transaction
    @Query("SELECT * from feed where type = 'TOP' order by update_date desc limit 10")
    abstract fun findTop10Feeds(): List<FeedWithSourcesEntity>?

    @Transaction
    @Query("SELECT * from feed where category_code = :category and page = :page order by update_date desc")
    abstract fun findFeedsByCategory(page: Int, category: String): List<FeedWithSourcesEntity>?

    @Query("SELECT * from related_feed where parent_id = :id order by update_date desc")
    abstract fun findRelatedFeeds(id: String): List<RelatedFeedWithSourcesEntity>?

    @Query("SELECT * from source")
    abstract fun findSources(): List<SourceEntity>?

    @Query("SELECT * from source")
    abstract fun findSourcesReactive(): Flow<List<SourceEntity>?>

    @Query("SELECT * from source where code = :code limit 1")
    abstract fun findSourceByCode(code: String): SourceEntity?

    @Query("SELECT * from category where code = :code limit 1")
    abstract fun findCategoryByCode(code: String): CategoryEntity?

    @Query("SELECT * from category where enable = 'Y'")
    abstract fun findCategories(): List<CategoryEntity>?

    @Query("SELECT * from category where enable = 'Y'")
    abstract fun findCategoriesReactive(): Flow<List<CategoryEntity>?>

    @Query("SELECT * from tag")
    abstract fun findTags(): List<TagEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRelatedFeeds(sources: List<RelatedFeedEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRelatedFeed(source: RelatedFeedEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSources(sources: List<SourceEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCategories(sources: List<CategoryEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTags(tags: List<TagEntity>): List<Long>

    @Query("DELETE from source")
    abstract fun deleteAllSources()

    @Query("DELETE from category")
    abstract fun deleteAllCategories()

    @Query("DELETE from tag")
    abstract fun deleteAllTags()

    @Query("DELETE from feed")
    abstract fun deleteAllFeed()

    @Query("DELETE from feed where type = :type ")
    abstract fun deleteAllLatestFeed(type: String)

    @Query("DELETE from feed where type = type = :type ")
    abstract fun deleteAllTopFeed(type: String)

    @Query("DELETE from feed where category_code = :category")
    abstract fun deleteAllFeedByCategory(category: String)
}