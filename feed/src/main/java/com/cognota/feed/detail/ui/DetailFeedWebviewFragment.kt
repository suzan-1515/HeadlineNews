package com.cognota.feed.detail.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.cognota.core.di.FeatureScope
import com.cognota.core.ui.BaseFragment
import com.cognota.feed.R
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import kotlinx.android.synthetic.main.fragment_feed_detail_webview.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@FeatureScope
class DetailFeedWebviewFragment : BaseFragment() {

    val args: DetailFeedWebviewFragmentArgs by navArgs()

    private var mAgentWeb: AgentWeb? = null

    override fun onAttach(context: Context) {
        Timber.d("attaching detail feed fragment")
        (activity as DetailFeedActivity).feedComponent
            .inject(this)
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_detail_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("webview fragment: onViewCreated")

        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                view as LinearLayout,
                -1,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator(ContextCompat.getColor(requireContext(), R.color.md_orange_500))
            .setWebChromeClient(mWebChromeClient)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            .interceptUnkownUrl()
            .createAgentWeb()
            .ready()
            .go(args.feed?.link)


        AgentWebConfig.debug()

        toolbar.setNavigationOnClickListener {
            it?.findNavController()?.navigateUp()
        }
        toolbar.inflateMenu(R.menu.menu_webview)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.refresh -> {
                    mAgentWeb?.urlLoader?.reload()
                    true
                }
                R.id.copy -> {
                    mAgentWeb?.let {
                        context?.let { it1 ->
                            toCopy(
                                it1,
                                it.webCreator.webView.url
                            )
                            Toast.makeText(it1, "Link copied to clipboard.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    true
                }
                R.id.default_browser -> {
                    mAgentWeb?.let {
                        openBrowser(it.webCreator.webView.url)
                    }
                    true
                }
                R.id.share -> {
                    args.feed?.let {
                        it.link()?.let { link ->
                            ShareCompat.IntentBuilder.from(activity)
                                .setType("text/plain")
                                .setChooserTitle("Share this news")
                                .setSubject(it.title)
                                .setText(
                                    requireContext().getString(
                                        R.string.news_share_format,
                                        it.title,
                                        link.toString()
                                    )
                                )
                                .startChooser()
                        }
                    }
                    true
                }
                else -> {
                    super.onOptionsItemSelected(menuItem)
                }
            }
        }


        mAgentWeb?.webCreator?.webView?.overScrollMode = WebView.OVER_SCROLL_NEVER

    }

    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)

            toolbar.title = title
        }

    }

    private fun openBrowser(targetUrl: String) {
        if (targetUrl.isEmpty() || targetUrl.startsWith("file://")) {
            Toast.makeText(context, "$targetUrl is invalid!", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.data = Uri.parse(targetUrl)
        startActivity(intent)
    }

    private fun toCopy(context: Context, text: String) {
        val mClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text))
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {

        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): DetailFeedWebviewFragment {
            return DetailFeedWebviewFragment()
        }
    }
}
