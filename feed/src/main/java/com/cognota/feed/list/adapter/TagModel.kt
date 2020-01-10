package com.cognota.feed.list.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target


@EpoxyModelClass(layout = R2.layout.item_tag)
abstract class TagModel(private val picasso: Picasso) :
    EpoxyModelWithHolder<TagModel.Holder>() {

    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    lateinit var icon: Uri
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: Holder) {
        holder.chip.text = title
        holder.chip.isCheckable = false
        if (::icon.isInitialized) {
            picasso.load(icon)
                .resize(32, 32)
                .onlyScaleDown()
                .centerCrop()
                .into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                    }

                    override fun onBitmapFailed(errorDrawable: Drawable?) {

                    }

                    override fun onBitmapLoaded(
                        bitmap: Bitmap,
                        from: LoadedFrom
                    ) {
                        holder.chip.chipIcon = bitmap.toDrawable(holder.chip.resources)
                    }
                })
        }

        if (::clickListener.isInitialized) {
            holder.chip.setOnClickListener(clickListener)
        }

    }

    override fun unbind(holder: Holder) {
        holder.chip.text = null
        holder.chip.setOnClickListener(null)
    }

    inner class Holder : BaseEpoxyHolder() {
        val chip by bind<Chip>(R.id.chip)
    }

}
