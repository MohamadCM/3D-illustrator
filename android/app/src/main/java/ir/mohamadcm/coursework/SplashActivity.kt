package ir.mohamadcm.coursework

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ir.mohamadcm.coursework.databinding.ActivityScanLauncherBinding
import ir.mohamadcm.coursework.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashAnimationView.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                val myIntent = Intent(this@SplashActivity, MainActivity::class.java)
                this@SplashActivity.startActivity(myIntent)
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