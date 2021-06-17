package com.ec.ardesignkitkat.ui.main

import android.os.Bundle
import android.preference.PreferenceActivity
import com.ec.ardesignkitkat.R

class SettingsActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)


    }
}