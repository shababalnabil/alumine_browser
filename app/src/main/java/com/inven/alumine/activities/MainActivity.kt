package com.inven.alumine.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inven.alumine.R
import com.inven.alumine.fragments.BrowserFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view, BrowserFragment(),"browser1")
            .commit()

    }

}