package com.kcn.jamthree

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import jamthree.Jam

class GameActivity : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type1 = intent?.getStringExtra("Type1").toString()
        val type2 = intent?.getStringExtra("Type2").toString()


        initialize(Jam(type1, type2), AndroidApplicationConfiguration())
    }
}