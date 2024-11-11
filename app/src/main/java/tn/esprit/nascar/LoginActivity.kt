package tn.esprit.nascar

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import tn.esprit.nascar.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.nascar.models.findedUser
import tn.esprit.nascar.models.User
import tn.esprit.nascar.utils.ApiInterface

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            doLogin()
        }

    }


    private fun doLogin() {
        val apiInterface = ApiInterface.create()
        val name = binding.loginEmail.text.toString()
        val phone = binding.loginPwd.text.toString()

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please enter both name and phone", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(name = name, phone = phone)

        apiInterface.login(user).enqueue(object : Callback<findedUser> {
            override fun onResponse(call: Call<findedUser>, response: Response<findedUser>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("LoginActivity", "Login response: $loginResponse")
                    if (loginResponse != null) {
                        saveUserToPreferences(loginResponse)
                        navigateToMainActivity()
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("LoginActivity", "Login failed: ${response.message()}")
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<findedUser>, t: Throwable) {
                Log.e("LoginActivity", "Connexion error!", t)
                Toast.makeText(this@LoginActivity, "Connexion error!", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun saveUserToPreferences(user: findedUser) {
        val sharedPref = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("userId", user._id)
            putString("userName", user.name)
            putString("userPhone", user.phone)
            apply()
        }
    }


    private fun navigateToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}