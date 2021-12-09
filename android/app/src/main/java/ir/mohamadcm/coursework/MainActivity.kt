package ir.mohamadcm.coursework

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.mohamadcm.coursework.databinding.ActivityMainBinding
import android.util.Patterns

import java.util.regex.Pattern
import java.util.regex.Pattern.matches


class MainActivity : AppCompatActivity() {

    // Binding object instance with access to the views in the activity_main.xml layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val queue = VolleySingleton.getInstance(this.applicationContext).requestQueue
        // Inflate the layout XML file and return a binding object instance
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Set the content view of the Activity to be the root view of the layout
        setContentView(binding.root)

        binding.progress.visibility = View.GONE
        // Setup a click listener on the calculate button to calculate the tip
        binding.buttonLogin.setOnClickListener(View.OnClickListener { view ->
            val emailText = binding.editEmail.editText?.text
            val passwordText = binding.editPassword.editText?.text

            binding.editEmail.editText?.error = null
            binding.editPassword.editText?.error = null

            val emailPattern = Patterns.EMAIL_ADDRESS
            val passPattern = Pattern.compile("/^.{6,}\$/")

            var isValid = true
            if (!emailPattern.matcher(emailText).matches()) {
                binding.editEmail.editText?.error = "Invalid Email"
                isValid = false
            }
            if (passwordText!!.length < 6) {
                binding.editPassword.editText?.error = "Invalid Password (6 Char At Least)"
                isValid = false
            }
            if (isValid) {
                binding.progress.visibility = View.VISIBLE
                val stringRequest = StringRequest(
                    Request.Method.GET, "https://nightly.smartfund.iknito.com/api/v1/md/user?username=${emailText}&password=${passwordText}",
                    Response.Listener<String> { response ->
                        binding.progress.visibility = View.GONE
                        Log.d("res", response)
                        // Display the first 500 characters of the response string.
                        Toast.makeText(applicationContext, "Response is: ${response.toString()}", Toast.LENGTH_LONG).show()
                    },
                    Response.ErrorListener { Toast.makeText(applicationContext, "Failed badly", Toast.LENGTH_LONG).show() })

                VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
            } else {
            }

//            Toast.makeText(applicationContext, "Login was Successful!", Toast.LENGTH_LONG).show()
//            val myIntent = Intent(this@MainActivity, ScannedBarcodeActivity::class.java)
//            myIntent.putExtra("user-name", 1) //Optional parameters
//            this@MainActivity.startActivity(myIntent)
        })

    }


}