package ir.mohamadcm.coursework

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.mohamadcm.coursework.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {

    // Binding object instance with access to the views in the activity_main.xml layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout XML file and return a binding object instance
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Set the content view of the Activity to be the root view of the layout
        setContentView(binding.root)

        // Setup a click listener on the calculate button to calculate the tip
        binding.buttonLogin.setOnClickListener(View.OnClickListener { view ->
            Toast.makeText(applicationContext, "Login was Successful!", Toast.LENGTH_LONG).show()
            val myIntent = Intent(this@MainActivity, ScannedBarcodeActivity::class.java)
            myIntent.putExtra("user-name", 1) //Optional parameters
            this@MainActivity.startActivity(myIntent)
        })

    }


}