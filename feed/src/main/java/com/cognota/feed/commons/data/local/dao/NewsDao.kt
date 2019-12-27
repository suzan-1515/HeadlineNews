package com.cognota.feed.commons.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.cognota.core.data.persistence.dao.BaseDao
import com.cognota.feed.list.data.entity.FeedEntity
import com.cognota.feed.list.data.relation.FeedWithRelatedEntity

@Dao
abstract class NewsDao : BaseDao<FeedEntity>() {

    @Transaction
    @Query("SELECT * from feed where type = 'latest' order by update_date desc")
    abstract fun findLatestFeeds(): List<FeedWithRelatedEntity>?

    @Query("SELECT * from feed where type = 'top' order by update_date desc")
    abstract fun findTopFeeds(): List<FeedEntity>?

    @Query("SELECT * from feed where category = :category order by update_date desc")
    abstract fun findFeedsByCategory(category: String): List<FeedEntity>?

    @Transaction
    open fun upsertAllFeedWithRelated(feeds: List<FeedWithRelatedEntity>) {
        feeds.map {
            insert(it.feed)
            it.relatedFeed?.let { relatedFeeds ->
                insert(relatedFeeds)
            }
        }
    }

    @Query("DELETE from feed")
    abstract fun deleteAllFeed()

    @Query("DELETE from feed where type = :type ")
    abstract fun deleteAllLatestFeed(type: String)

    @Query("DELETE from feed where type = type = :type ")
    abstract fun deleteAllTopFeed(type: String)

    @Query("DELETE from feed where category = :category")
    abstract fun deleteAllFeedByCategory(category: String)
}