package com.example.homework

import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


const val URL = "https://espresso-food-delivery-backend-cc3e106e2d34.herokuapp.com"
class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView2)
        AsyncTask.execute{
            sendLoginRequest("fatih1@gmail.com","Gptmap123")
            sendGetRequest("64a333cf-4e25-4504-8e93-2698e9fa5413")
        }
    }
    fun sendLoginRequest(email:String, password:String) {
        try {
            println("AAAAA")
            val url = URL(URL+"/login")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                doOutput = true // Indicates this request will have an output.

                setRequestProperty("Content-Type", "application/json")

                // Build JSON payload
                val jsonBody = "{\"email\":\"$email\",\"password\":\"$password\"}"


                // Send request
                val wr = OutputStreamWriter(outputStream)
                wr.write(jsonBody)
                wr.flush()

                // Check response code
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read response
                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()
                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        // Process response here
                        println("Response : $response")
                    }
                } else {
                    println("Server returned non-OK status: $responseCode")
                    // Handle error
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exception
        }
    }

    fun sendGetRequest(profile: String) {
        try {
            val reqParam =URLEncoder.encode(profile, "UTF-8")
            val mURL = URL("$URL/profile/$reqParam")

            with(mURL.openConnection() as HttpURLConnection) {
                // optional default is GET
                requestMethod = "GET"

                println("URL : $url")
                println("Response Code : $responseCode")

                // Check if the request was successful (status code 200)
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read response
                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()
                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }

                        // Convert JSON response to Profile object using Gson
                        val profile = Gson().fromJson(response.toString(), Profile::class.java)

                        // Now you can access individual fields of the profile object
                        println("Profile: $profile")
                        textView.text = profile.toString()
                    }
                } else {
                    println("Server returned non-OK status: $responseCode")
                    // Handle error
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exception
        }
    }


}