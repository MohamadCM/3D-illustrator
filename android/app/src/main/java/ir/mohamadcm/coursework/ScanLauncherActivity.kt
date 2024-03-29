package ir.mohamadcm.coursework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ir.mohamadcm.coursework.databinding.ActivityScanLauncherBinding

class ScanLauncherActivity : AppCompatActivity() {

    var myPref: String  = "MyPrefsFile"
    private lateinit var binding: ActivityScanLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_launcher)

        binding = ActivityScanLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.launchScan.setOnClickListener(View.OnClickListener { view ->
            val myIntent = Intent(this@ScanLauncherActivity, ScannedBarcodeActivity::class.java)
            this@ScanLauncherActivity.startActivity(myIntent)
        })

        binding.buttonOpt.setOnClickListener(View.OnClickListener { view ->
            val myIntent = Intent(this@ScanLauncherActivity, MoreOptionsActivity::class.java)
            this@ScanLauncherActivity.startActivity(myIntent)
        })

        binding.logoutButton.setOnClickListener(View.OnClickListener { view ->
            val editor = getSharedPreferences(myPref, MODE_PRIVATE).edit()
            editor.putString("isLoggedIn", "no");
            editor.apply();

            val myIntent = Intent(this@ScanLauncherActivity, MainActivity::class.java)
            this@ScanLauncherActivity.startActivity(myIntent)
            finish()
        })
    }
}