package com.inven.alumine.components.toolbar.listener

interface KeyboardVisibilityListener {
    fun onShowKeyboard()
    fun onHideKeyboard()
    fun onEnterPressed(text : String)
}