package com.inven.alumine.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inven.alumine.R
import com.inven.alumine.components.toolbar.Util
import com.inven.alumine.components.toolbar.listener.KeyboardVisibilityListener
import com.inven.alumine.components.toolbar.listener.TextChangeListener
import com.inven.alumine.components.toolbar.views.BrowserToolbar
import com.inven.alumine.components.web.engine.WebEngine
import com.inven.alumine.fragments.listener.InteractionListener
import com.inven.alumine.suggestion.SuggestionProvider
import com.inven.alumine.suggestion.listener.ItemClickListener
import com.inven.alumine.suggestion.listener.SuggestionResponseListener
import com.inven.alumine.suggestion.ui.SuggestionAdapter
import com.inven.alumine.utils.app.SearchManager

open class BaseBrowserFragment : Fragment(), InteractionListener, KeyboardVisibilityListener,
    TextChangeListener, SuggestionResponseListener, ItemClickListener {
    protected val toolbar: BrowserToolbar by lazy { requireView().findViewById(R.id.toolbar) }
    private val suggestionRecyclerView: RecyclerView by lazy {
        requireView().findViewById<RecyclerView?>(
            R.id.suggestion_recycler_view
        ).apply { layoutManager = LinearLayoutManager(requireContext()) }
    }
    private val top_window: View by lazy { requireView().findViewById(R.id.top_window) }
    private val suggestionProvider: SuggestionProvider by lazy { SuggestionProvider() }
    protected val webEngine: WebEngine by lazy { requireView().findViewById(R.id.webEngine) }
    private val engine_view: View by lazy { requireView().findViewById(R.id.engine_view) }
    private val errorLayout: View by lazy { requireView().findViewById(R.id.error_view) }
    private val errorHeader: TextView by lazy { requireView().findViewById(R.id.error_header) }
    private val errorDescription: TextView by lazy { requireView().findViewById(R.id.error_des) }
    private val errorButton: TextView by lazy { requireView().findViewById(R.id.error_button) }
    private val errorImageView: ImageView by lazy { requireView().findViewById(R.id.error_icon) }
    private val subHomeLayout: View by lazy { requireView().findViewById(R.id.sub_home_layout) }
    private val suggestionAdapter: SuggestionAdapter by lazy {
        SuggestionAdapter().also {
            suggestionRecyclerView.adapter = it
        }
    }

    private var rootView : View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_browser, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().onBackPressedDispatcher.addCallback {
            onBackPressed()
        })
        suggestionProvider.setSuggestionResponseListener(this)
        toolbar.keyboardVisibilityListener = this
        toolbar.textChangeListener = this
        top_window.layoutParams =
            ViewGroup.LayoutParams(-1, Util.getStatusBarHeight() + Util.getPix(57f))
        errorButton.setOnClickListener {
            if (errorButton.text == "Try again!") {
                webEngine.loadUrl(toolbar.edittext.text.toString())
            } else {
                webEngine.loadUrl("https://www.google.com/")
                hideErrorPage()
            }
        }

        suggestionAdapter.itemClickListener = this

    }


    override fun onBrowsingStart(initUri: String?) {
        toolbar.toolbarLoaded()
    }


    override fun onBackPressed() {
        if (toolbar.canGoBack) {
            toolbar.canGoBack = false
            return
        }
        if (webEngine.canGoBack) {
            webEngine.goBack()
            return
        }
        requireActivity().finish()
    }

    override fun onShowKeyboard() {
        suggestionRecyclerView.visibility = View.VISIBLE
        top_window.setBackgroundResource(R.drawable.focused_status_bar)
    }

    override fun onHideKeyboard() {
        top_window.setBackgroundResource(R.drawable.toolbar_background)
        suggestionRecyclerView.visibility = View.GONE
    }

    override fun onEnterPressed(text: String) {
        engine_view.visibility = View.VISIBLE
        requireView().findViewById<ConstraintLayout>(R.id.main_content).removeView(subHomeLayout)
        onBrowsingStart(SearchManager.getSearchOrLoad(text))
    }

    fun updateProgress(progress: Int) {
        toolbar.setProgress(progress)
    }

    fun showProgressBar() {
        toolbar.apply {
            showStopButton()
            changeProgressBarVisibility(View.VISIBLE)
        }

    }

    fun hideProgressBar() {
        toolbar.apply {
            showReloadButton()
            updateProgress(0)
            changeProgressBarVisibility(View.GONE)
        }
    }

    fun updateUrl(url: String) {
        toolbar.updateUrl(url)
    }

    fun showErrorPage(isBlocked: Boolean) {
        if (isBlocked) {
            errorButton.text = "Alright"
            errorHeader.text = "Restricted Content!"
            errorDescription.text = getString(R.string.blocked_error)
            errorImageView.setImageResource(R.drawable.round_gpp_bad_24)
        } else {
            errorHeader.text = "Network problem!"
            errorButton.text = "Try again!"
            errorDescription.text = getString(R.string.normal_error)
            errorImageView.setImageResource(R.drawable.round_cloud_off_24)
        }

        errorLayout.visibility = View.VISIBLE
        webEngine.visibility = View.INVISIBLE
    }

    fun hideErrorPage() {
        errorLayout.visibility = View.GONE
        webEngine.visibility = View.VISIBLE
    }

    override fun onTextChanged(text: String) {
        if (text.isBlank()) {
            suggestionProvider.clear()
            suggestionAdapter.updateSuggestionList(null)
        }
        suggestionProvider.requestSuggestions(text)
    }

    override fun onGetSuggestions(suggestions: String) {
        if (toolbar.edittext.text!!.isBlank()) {
            suggestionAdapter.updateSuggestionList(null)
            return
        }
        suggestionAdapter.updateSuggestionList(suggestions)
    }

    override fun onItemClick(text: String) {
        toolbar.apply {
            toolbarLoaded()
            if (edittext.hasFocus()){
                clearFocus()
            }
            toolbar.onEnterPressed(text)
        }
    }

    override fun onPickButtonClick(text: String) {
        toolbar.edittext.apply {
            setText(text.trim())
            setSelection(length())
        }
    }

}