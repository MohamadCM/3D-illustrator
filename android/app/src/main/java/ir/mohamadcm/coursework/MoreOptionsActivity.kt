package ir.mohamadcm.coursework

import android.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.smarteist.autoimageslider.SliderView
import ir.mohamadcm.coursework.databinding.ActivityMoreOptionsBinding
import kotlinx.coroutines.delay
import java.sql.Time
import kotlin.math.ceil
import kotlin.random.Random


class MoreOptionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoreOptionsBinding
    private var activateButton = false;
    val imageList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageTheme()

        val imageSlider = binding.imageSlider

        imageList.add("https://i.ibb.co/dryXzk0/truck.png")
        imageList.add("https://i.ibb.co/8XgfbQC/engine.png")
        imageList.add("https://i.ibb.co/Pjs55cY/box.png")
        setImageInSlider(imageList, imageSlider)

        val selectButton = binding.buttonFile;
        selectButton.setOnClickListener { launchFiles() }

        val uploadButton = binding.buttonSend;
        uploadButton.setOnClickListener { upload() }

    }

    /* Switch between dark and light mode */
    fun manageTheme() {
        val switchBtn = binding.buttonThemeSwitch
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            switchBtn.isChecked = true
        switchBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun launchFiles() {
        binding.imageQr.isVisible = false;
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)

        Handler().postDelayed({
            activateButton = true;
            binding.buttonSend.isEnabled = activateButton
        },4000 )
    }

    private fun upload() {
        binding.progressBar.isVisible = true;
        val rand = Random.nextDouble()
        Handler().postDelayed({
            binding.progressBar.isVisible = false;
            activateButton = false;
            binding.buttonSend.isEnabled = activateButton;
            binding.imageQr.isVisible = true;
        },ceil(rand * 5000).toLong())
        Toast.makeText(applicationContext, "Upload was successful!", Toast.LENGTH_LONG).show()
        imageList.add("https://i.ibb.co/DMXrB9Q/avocado.png")
        setImageInSlider(imageList, binding.imageSlider)
    }

    private fun setImageInSlider(images: ArrayList<String>, imageSlider: SliderView) {
        val adapter = MySliderImageAdapter()
        adapter.renewItems(images)
        imageSlider.setSliderAdapter(adapter)
        imageSlider.isAutoCycle = true
        imageSlider.startAutoCycle()
    }
}