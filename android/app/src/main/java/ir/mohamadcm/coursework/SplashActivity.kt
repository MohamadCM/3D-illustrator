package ir.mohamadcm.coursework

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ir.mohamadcm.coursework.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    var myPref: String  = "MyPrefsFile"
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val prefs = getSharedPreferences(myPref, MODE_PRIVATE)
        val isLoggedIn = prefs.getString("isLoggedIn", "no")

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashAnimationView.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (isLoggedIn == "yes") {
                    val myIntent = Intent(this@SplashActivity, ScanLauncherActivity::class.java)
                    this@SplashActivity.startActivity(myIntent)
                    finish()
                } else if (isLoggedIn == "no") {
                    val myIntent = Intent(this@SplashActivity, MainActivity::class.java)
                    this@SplashActivity.startActivity(myIntent)
                    finish()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    //
//        binding.launchScan.setOnClickListener(View.OnClickListener { view ->
//            val myIntent = Intent(this@SplashActivity, ScannedBarcodeActivity::class.java)
//            this@SplashActivity.startActivity(myIntent)
//        })
//
//        binding.buttonOpt.setOnClickListener(View.OnClickListener { view ->
//            val myIntent = Intent(this@SplashActivity, MoreOptionsActivity::class.java)
//            this@SplashActivity.startActivity(myIntent)
//        })
    }
}