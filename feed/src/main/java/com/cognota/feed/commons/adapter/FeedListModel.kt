package com.cognota.feed.commons.adapter

import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2
import com.cognota.feed.commons.domain.FeedType
import com.cognota.feed.personalised.ui.PersonalisedFeedFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


@EpoxyModelClass(layout = R2.layout.item_list_feed)
abstract class FeedListModel(private val picasso: Picasso) :
    EpoxyModelWithHolder<FeedListModel.Holder>() {

    @EpoxyAttribute
    lateinit var uuid: String
    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    lateinit var image: Uri
    @EpoxyAttribute
    lateinit var sourceIcon: Uri
    @EpoxyAttribute
    var preview: String? = null
    @EpoxyAttribute
    lateinit var date: String
    @EpoxyAttribute
    lateinit var source: String
    @EpoxyAttribute
    lateinit var category: String
    @EpoxyAttribute
    lateinit var type: FeedType

    override fun bind(holder: Holder) {
        title.let { holder.title.text = it }
        preview?.let { holder.preview.text = it }
        if (::date.isInitialized && ::source.isInitialized)
            holder.date.text =
                holder.date.context.getString(R.string.source_with_time, source, date)
        if (::image.isInitialized) {
            picasso.load(image)
                .fit()
                .transform(RoundedCornersTransformation(16, 0))
                .into(holder.image, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError() {
                        holder.image.visibility = View.GONE
                    }

                })
        } else {
            holder.image.visibility = View.GONE
        }
        if (::sourceIcon.isInitialized) {
            picasso.load(sourceIcon)
                .resize(64, 64)
                .onlyScaleDown()
                .centerInside()
                .into(holder.sourceIcon)
        }
        if (::category.isInitialized) {
            holder.category.text = category
        } else {
            holder.category.text = "N/A"
        }
        holder.card.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                holder.title to holder.title.transitionName,
                holder.preview to holder.preview.transitionName,
                holder.date to holder.date.transitionName,
                holder.image to holder.image.transitionName,
                holder.sourceIcon to holder.sourceIcon.transitionName,
                holder.category to holder.category.transitionName
            )
            it.findNavController().navigate(
                PersonalisedFeedFragmentDirections.detailAction(
                    id = uuid,
                    title = title,
                    image = image.toString(),
                    description = preview,
                    publishedDate = date,
                    sourceTitle = source,
                    sourceIcon = sourceIcon.toString(),
                    categoryTitle = category,
                    type = type
                ),
                extras
            )
        }

        ViewCompat.setTransitionName(holder.title, "title$uuid")
        ViewCompat.setTransitionName(holder.preview, "preview$uuid")
        ViewCompat.setTransitionName(holder.date, "date$uuid")
        ViewCompat.setTransitionName(holder.image, "image$uuid")
        ViewCompat.setTransitionName(holder.sourceIcon, "source_icon$uuid")
        ViewCompat.setTransitionName(holder.category, "category$uuid")
    }

    override fun unbind(holder: Holder) {
        holder.title.text = null
        holder.preview.text = null
        holder.date.text = null
        holder.category.text = null
        picasso.cancelRequest(holder.image)
        picasso.cancelRequest(holder.sourceIcon)
        ViewCompat.setTransitionName(holder.title, null)
        ViewCompat.setTransitionName(holder.preview, null)
        ViewCompat.setTransitionName(holder.date, null)
        ViewCompat.setTransitionName(holder.image, null)
        ViewCompat.setTransitionName(holder.sourceIcon, null)
        ViewCompat.setTransitionName(holder.category, null)
    }

    override fun shouldSaveViewState(): Boolean {
        return true
    }

    inner class Holder : BaseEpoxyHolder() {
        val card by bind<MaterialCardView>(R.id.card)
        val image by bind<AppCompatImageView>(R.id.image)
        val sourceIcon by bind<CircleImageView>(R.id.sourceIcon)
        val title by bind<TextView>(R.id.title)
        val preview by bind<TextView>(R.id.preview)
        val date by bind<TextView>(R.id.date)
        val category by bind<AppCompatTextView>(R.id.category)
    }

}
