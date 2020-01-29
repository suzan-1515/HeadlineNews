package com.cognota.feed.commons.data.local.dao

import androidx.room.*
import com.cognota.core.data.persistence.dao.BaseDao
import com.cognota.feed.commons.data.local.entity.*
import com.cognota.feed.commons.data.local.relation.FeedWithSourcesEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NewsDao : BaseDao<FeedEntity>() {

    @Transaction
    @Query("SELECT * from feed where type = 'LATEST' and parent_id is null and enabled = 1")
    abstract fun findLatestFeeds(): List<FeedWithSourcesEntity>?

    @Transaction
    @Query("SELECT * from feed where type = 'TOP' and enabled = 1")
    abstract fun findTopFeeds(): List<FeedWithSourcesEntity>?

    @Transaction
    @Query("SELECT * from feed where type = 'TOP' and enabled = 1 limit 10")
    abstract fun findTop10Feeds(): List<FeedWithSourcesEntity>?

    @Transaction
    @Query("SELECT * from feed where category_code = :category and page = :page and enabled = 1")
    abstract fun findFeedsByCategory(
        page: Int,
        category: String
    ): List<FeedWithSourcesEntity>?

    @Transaction
    @Query("SELECT * from feed where id = :id limit 1")
    abstract fun findFeed(
        id: String
    ): FeedWithSourcesEntity?

    @Transaction
    @Query("SELECT * from feed where parent_id = :parentId and enabled = 1")
    abstract fun findRelatedFeeds(
        parentId: String
    ): Flow<List<FeedWithSourcesEntity>?>

    @Query("SELECT * from bookmark")
    abstract fun findBookmarkedFeeds(): Flow<List<BookmarkEntity>?>

    @Query("SELECT * from bookmark where id= :id limit 1")
    abstract fun findBookmarkedFeed(id: String): Flow<BookmarkEntity?>

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
    abstract fun insertSources(sources: List<SourceEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCategories(sources: List<CategoryEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTags(tags: List<TagEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertBookmarkFeed(bookmarkEntity: BookmarkEntity): Long

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

    @Query("DELETE from bookmark where id = :id ")
    abstract fun deleteBookmarkFeed(id: String)

    @Query("DELETE from bookmark")
    abstract fun deleteAllBookmarkFeed()
}