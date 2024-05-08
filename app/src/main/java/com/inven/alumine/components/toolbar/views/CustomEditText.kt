package com.inven.alumine.components.toolbar.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.inven.alumine.components.toolbar.listener.KeyboardVisibilityListener
import com.inven.alumine.filter.CleanContent

class CustomEditText : AppCompatEditText {
    var keyboardVisibilityListener: KeyboardVisibilityListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        keyboardVisibilityListener?.apply {
            if (focused) {
                onShowKeyboard()
            } else {
                onHideKeyboard()
                val imm = ContextCompat.getSystemService(context,InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(windowToken,0)
            }
        }
    }

    override fun onEditorAction(actionCode: Int) {
        super.onEditorAction(actionCode)
        if (actionCode==6){
            keyboardVisibilityListener?.onEnterPressed(text.toString())
        }
    }


    override fun setText(text: CharSequence?, type: BufferType?) {
        if (CleanContent.contains(text.toString())){
            super.setText("about:blank", type)
            return
        }
        super.setText(text, type)
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (hasFocus()){
            clearFocus()
        }
        return !(keyCode == KeyEvent.KEYCODE_BACK && event?.getAction() == KeyEvent.ACTION_UP)
    }

}
