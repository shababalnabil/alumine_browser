package com.inven.alumine.components.toolbar.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.inven.alumine.R
import com.inven.alumine.components.toolbar.Util
import com.inven.alumine.components.toolbar.listener.KeyboardVisibilityListener
import com.inven.alumine.components.toolbar.listener.TextChangeListener
import com.inven.alumine.filter.CleanContent
import com.inven.alumine.utils.app.SearchManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.newCoroutineContext
import kotlin.coroutines.coroutineContext

class BrowserToolbar : FrameLayout, KeyboardVisibilityListener {
    var keyboardVisibilityListener: KeyboardVisibilityListener? = null
    val edittext: CustomEditText by lazy { findViewById(R.id.customEdittext) }
    private val navigationLayout: View by lazy { findViewById(R.id.navigationLayout) }
    val menuButton: View by lazy { findViewById(R.id.menu_browser) }
    private val divider: View by lazy { findViewById(R.id.divider_icons) }
    private val mainContent: ConstraintLayout by lazy { findViewById(R.id.main_content_toolbar) }
    private val progressbarParent: View by lazy { findViewById(R.id.progress_bar_parent) }
    private val edittextClear: View by lazy { findViewById(R.id.edittext_clear) }
    val reloadButton : View by lazy { findViewById(R.id.reload_button) }
    val stopButton : View by lazy { findViewById(R.id.stop_button) }
    private val progressIndicator: LinearProgressIndicator by lazy { findViewById(R.id.progressIndicator) }
    private val actionView : ImageView by lazy { findViewById(R.id.action_view) }
    private val edge_fade : View by lazy { findViewById(R.id.fade_edge) }
    var canGoBack = false
    private var isLoaded = false
    var textChangeListener : TextChangeListener? = null
    private var currentUrl = ""

    fun toolbarLoaded(){
        isLoaded = true
        navigationLayout.visibility = View.VISIBLE
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        inflate(context, R.layout.toolbar_browser, this)
        edittext.apply {
            keyboardVisibilityListener = this@BrowserToolbar
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edittext.hasFocus()) {
                        textChangeListener?.onTextChanged(s?.toString()!!)
                        updateActionView(s.toString())
                        if (s.toString().isNotEmpty()) {
                            edittextClear.visibility = View.VISIBLE
                        } else {
                            edittextClear.visibility = View.GONE
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
        edittextClear.setOnClickListener {
            edittext.setText("")
        }

    }

    private fun updateActionView(text : String){
        if (text.isBlank()){
            actionView.setImageResource(R.drawable.round_search_24)
            return
        }
        if (SearchManager.isUrl(text)){
            actionView.setImageResource(R.drawable.round_public_24)
            return
        }
        actionView.setImageResource(R.drawable.round_search_24)
    }

    override fun onShowKeyboard() {
        edge_fade.visibility = View.GONE
        actionView.visibility = View.VISIBLE
        updateActionView(edittext.text.toString())
        if (edittext.text.toString().isNotEmpty()) {
            edittextClear.visibility = View.VISIBLE
        }
        navigationLayout.visibility = View.GONE
        menuButton.visibility = View.GONE
        divider.visibility = View.INVISIBLE
        canGoBack = true
        keyboardVisibilityListener?.onShowKeyboard()
        progressbarParent.visibility = View.GONE
        mainContent.apply {
            setBackgroundResource(R.drawable.toolbar_input_background_focused)
            Util.apply {
                layoutParams = LinearLayout.LayoutParams(-1, -2).apply {
                    setMargins(getPix(4f), getPix(4f), getPix(4f), getPix(4f))
                }
            }
        }
    }

    override fun onHideKeyboard() {
        edge_fade.visibility = View.VISIBLE
        actionView.visibility = View.GONE
        edittext.setText(currentUrl)
        canGoBack = false
        edittextClear.visibility = View.GONE
        progressbarParent.visibility = View.VISIBLE
        mainContent.apply {
            setBackgroundResource(R.drawable.toolbar_input_background)
            Util.apply {
                (layoutParams as LinearLayout.LayoutParams).apply {
                    width = 0
                    weight = 1f
                    setMargins(getPix(10f), getPix(6f), getPix(4f), getPix(6f))
                }
            }
        }
        keyboardVisibilityListener?.onHideKeyboard()
        menuButton.visibility = View.VISIBLE
        if (isLoaded) {
            navigationLayout.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
        }
    }

    override fun onEnterPressed(text: String) {
        if (text.isBlank() || text == "null") {
            return
        }
        val imm = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(windowToken, 0)
        keyboardVisibilityListener?.onEnterPressed(text)
    }

    fun setProgress(progress: Int) {
        progressIndicator.progress = progress
    }

    fun changeProgressBarVisibility(visibility: Int){
        progressIndicator.visibility = visibility
    }

    fun updateUrl(url : String){
        currentUrl = url
        if (!edittext.hasFocus()){
            edittext.setText(if (CleanContent.contains(currentUrl)){"about:blank"}else{currentUrl})
        }
    }

    fun showReloadButton(){
        stopButton.visibility = View.GONE
        reloadButton.visibility = View.VISIBLE
    }

    fun showStopButton(){
        stopButton.visibility = View.VISIBLE
        reloadButton.visibility = View.GONE
    }

}
