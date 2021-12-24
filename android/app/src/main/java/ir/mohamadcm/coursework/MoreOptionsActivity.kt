package ir.mohamadcm.coursework

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ir.mohamadcm.coursework.databinding.ActivityMoreOptionsBinding


class MoreOptionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoreOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageTheme()

    }

    /* Switch between dark and light mode */
    fun manageTheme(){
        val switchBtn = binding.buttonThemeSwitch
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            switchBtn.isChecked = true
        switchBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}