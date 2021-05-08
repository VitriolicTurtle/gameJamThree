package com.kcn.jamthree

import android.content.Intent
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration


/** Launches the Android application.  */
// TOPIC:    Magic Gone Wrong

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(null, AndroidApplicationConfiguration())


        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
    }
}