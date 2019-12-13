package com.cognota.headlinenews.list

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
import com.cognota.headlinenews.R
import com.cognota.headlinenews.commons.domain.FeedDTO
import com.cognota.headlinenews.list.di.ListScope
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.feed_item.view.*
import timber.log.Timber
import javax.inject.Inject

@ListScope
class FeedListAdapter @Inject constructor(private val picasso: Picasso) :
    ListAdapter<FeedDTO, FeedListAdapter.ListViewHolder>(FeedDC()) {

    var interaction: Interaction? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ListViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false), interaction
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
                itemView.tvTitle,
                itemView.tvBody,
                itemView.tvAuthorName,
                itemView.ivAvatar
            )
        }

        fun bind(item: FeedDTO, picasso: Picasso) = with(itemView) {
            tvTitle.text = item.title
            tvBody.text = item.description
            tvAuthorName.text = item.publishedDate.toString()
            Timber.d("Feed image url: %s", item.image)
            picasso.load(item.image)
                .into(itemView.ivAvatar)

            //SharedItem transition
            ViewCompat.setTransitionName(tvTitle, item.title)
            ViewCompat.setTransitionName(tvBody, item.description)
            ViewCompat.setTransitionName(tvAuthorName, item.source)
            ViewCompat.setTransitionName(ivAvatar, item.image)
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
            oldItem.id.equals(newItem.id)

        override fun areContentsTheSame(oldItem: FeedDTO, newItem: FeedDTO) =
            oldItem == newItem
    }
}