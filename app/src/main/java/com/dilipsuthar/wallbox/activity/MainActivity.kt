package com.dilipsuthar.wallbox.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.utils.ThemeUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    @BindView(R.id.txt_bottom_msg) lateinit var txtBottomMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        hideStatusBar()

        //txt_bottom_msg.text = "Made from ${Character.toChars(0x2764)} by, Dilip Suthar"
        //txtBottomMsg.text = String(Character.toChars(0x1F496))

        txtBottomMsg.text = (StringBuilder()
            .append("Made with ")
            .append(String(Character.toChars(0x2764)))
            .append(" by DILIP SUTHAR"))

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
