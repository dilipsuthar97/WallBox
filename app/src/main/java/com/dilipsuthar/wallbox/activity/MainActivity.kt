package com.dilipsuthar.wallbox.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.utils.ThemeUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideStatusBar()

        txt_bottom_msg.text = "Made from ${Character.toChars(0x2764)} by, Dilip Suthar"

        // Start HomeActivity after 800 millisec
        Handler().postDelayed(
            {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                finish()
            },
            800
        )

    }

    private fun hideStatusBar() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

}
