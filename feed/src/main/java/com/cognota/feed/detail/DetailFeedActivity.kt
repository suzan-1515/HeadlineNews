package com.cognota.feed.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.cognota.feed.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_feed_detail.*

class DetailFeedActivity : AppCompatActivity() {

    val args: DetailFeedActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
//        val appComponent = (applicationContext as CoreApp).coreComponent
//        DaggerFeedComponent.factory()
//            .create(appComponent)
//            .inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feed_detail)

        prepareSharedTransition()
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp) // need to set the icon here to have a navigation icon. You can simple create an vector image by "Vector Asset" and using here
        toolbar.setNavigationOnClickListener {
            it.findNavController().popBackStack()
        }

        toolbar.inflateMenu(R.menu.menu_feed_detail)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_bookmark -> {
                    Toast.makeText(this, "Bookmark menu clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_share -> {
                    Toast.makeText(this, "Share menu clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        args.image?.let { Picasso.with(this).load(it).into(image) }
        args.title?.let { findViewById<AppCompatTextView>(R.id.title).text = it }
        args.description?.let { description.text = it }

    }

    private fun prepareSharedTransition() {
        args.id?.let { id ->
            args.title?.let {
                ViewCompat.setTransitionName(
                    findViewById<AppCompatTextView>(R.id.title),
                    "title$id"
                )
            }
            args.description?.let {
                ViewCompat.setTransitionName(description, "preview$id")
            }
            args.image?.let {
                ViewCompat.setTransitionName(image, "image$id")
            }
        }
    }

}
