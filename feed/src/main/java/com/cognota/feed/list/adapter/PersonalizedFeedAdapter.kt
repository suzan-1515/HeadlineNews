package com.cognota.feed.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cognota.feed.R
import com.cognota.feed.commons.domain.FeedDTO
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_feed.view.*
import timber.log.Timber
import javax.inject.Inject

class PersonalizedFeedAdapter @Inject constructor(private val picasso: Picasso) :
    ListAdapter<FeedDTO, PersonalizedFeedAdapter.ListViewHolder>(
        FeedDC()
    ) {

    var interaction: Interaction? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ListViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed, parent, false), interaction
    )

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) = holder.bind(getItem(position), picasso)

    fun swapData(data: List<FeedDTO>) {
        submitList(data.toMutableList())
    }

    inner class ListViewHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val clicked = getItem(adapterPosition)
            interaction?.feedClicked(
                clicked,
                itemView.title,
                itemView.preview,
                itemView.date,
                itemView.image
            )
        }

        fun bind(item: FeedDTO, picasso: Picasso) = with(itemView) {
            title.text = item.title
            preview.text = item.description
            date.text = item.publishedDate.toString()
            Timber.d("Feed image url: %s", item.image)
            picasso.load(item.image)
                .into(itemView.image)

            //SharedItem transition
            ViewCompat.setTransitionName(title, item.title)
            ViewCompat.setTransitionName(preview, item.description)
            ViewCompat.setTransitionName(date, item.source)
            ViewCompat.setTransitionName(image, item.image.toString())
        }
    }

    interface Interaction {
        fun feedClicked(
            feed: FeedDTO,
            tvTitle: TextView,
            tvBody: TextView,
            tvAuthorName: TextView,
            ivAvatar: ImageView
        )
    }

    private class FeedDC : DiffUtil.ItemCallback<FeedDTO>() {
        override fun areItemsTheSame(oldItem: FeedDTO, newItem: FeedDTO) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FeedDTO, newItem: FeedDTO) =
            oldItem == newItem
    }
}