package ir.mohamadcm.coursework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.mohamadcm.coursework.databinding.ActivityMainBinding
import android.util.Patterns
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

import java.util.regex.Pattern
import android.content.SharedPreferences





class MainActivity : AppCompatActivity() {

    var myPref: String  = "MyPrefsFile"
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

        // Set Default Theme to light
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Login Button Logic
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
                    { response ->
                        binding.progress.visibility = View.GONE
                        if (response.toBoolean()) { // Login Successful
                            Toast.makeText(applicationContext, "Logged In Successfully!", Toast.LENGTH_LONG).show()

                            val editor = getSharedPreferences(myPref, MODE_PRIVATE).edit()
                            editor.putString("isLoggedIn", "yes");
                            editor.apply();

                            val myIntent = Intent(this@MainActivity, ScanLauncherActivity::class.java)
                            this@MainActivity.startActivity(myIntent)
                            finish()
                        } else { // Login Failed
                            Toast.makeText(applicationContext, "Wrong Credentials, Try Again!", Toast.LENGTH_LONG).show()
                        }
                    },
                    {
                        binding.progress.visibility = View.GONE
                        Toast.makeText(applicationContext, "Something Went Wrong!", Toast.LENGTH_LONG).show()
                    })
                VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
            }
//            myIntent.putExtra("user-name", 1) //Optional parameters
        })

        // Register Button Logic
        binding.buttonRegister.setOnClickListener(View.OnClickListener { view ->
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
                    Request.Method.POST, "https://nightly.smartfund.iknito.com/api/v1/md/user?username=${emailText}&password=${passwordText}",
                    { response ->
                        binding.progress.visibility = View.GONE
                        if (response.toBoolean()) { // Register Successful
                            val editor = getSharedPreferences(myPref, MODE_PRIVATE).edit()
                            editor.putString("isLoggedIn", "yes");
                            editor.apply();
                            
                            Toast.makeText(applicationContext, "Registration Was Successful!", Toast.LENGTH_LONG).show()
                            val myIntent = Intent(this@MainActivity, ScanLauncherActivity::class.java)
                            this@MainActivity.startActivity(myIntent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Registration Failed, Try Again!", Toast.LENGTH_LONG).show()
                        }
                    },
                    {
                        binding.progress.visibility = View.GONE
                        Toast.makeText(applicationContext, "Something Went Wrong!", Toast.LENGTH_LONG).show()
                    })
                VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
            }
        })
    }


}