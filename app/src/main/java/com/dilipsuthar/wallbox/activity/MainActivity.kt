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

/**
 * Created by,
 * @author DILIP SUTHAR 05/06/19
 */

class MainActivity : BaseActivity() {

    @BindView(R.id.txt_bottom_msg) lateinit var txtBottomMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        hideStatusBar()

        txtBottomMsg.text = (StringBuilder()
            .append("Made with ")
            .append(String(Character.toChars(0x2764)))
            .append(" by ${resources.getString(R.string.owner_name)}"))

        // Start HomeActivity after 800 millisec
        Handler().postDelayed(
            {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                finish()
            },
            800
        )

    }

    /** @method hide system UI */
    private fun hideStatusBar() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }



}
