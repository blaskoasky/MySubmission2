package com.example.mysubmission2

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission2.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception



class MainActivity : AppCompatActivity() {
    var list: ArrayList<UserData> = ArrayList()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListUserAdapter

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListUserAdapter(list)
        binding.rvMain.setHasFixedSize(true)

        getUser()
    }

    private fun showRecyclerList() {
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        binding.rvMain.adapter = adapter
    }

    private fun getUser() {
        binding.progressBar.visibility = View.VISIBLE
        val aClient = AsyncHttpClient()
        val url = "https://api.github.com/users"
        aClient.addHeader("Authorization", "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")
        aClient.addHeader("User-Agent", "request")
        aClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String? = jsonObject.getString("login")
                        setUser(username)
                    }
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun userQuery(q: String?) {
        binding.progressBar.visibility = View.INVISIBLE

        val aClient = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$q"
        aClient.addHeader("Authorization", "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")
        aClient.addHeader("User-Agent", "request")
        aClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()){
                        val jsonObject = item.getJSONObject(i)
                        val username: String? = jsonObject.getString("login")
                        setUser(username)
                    }
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun setUser(id: String?){
        binding.progressBar.visibility = View.VISIBLE

        val aClient = AsyncHttpClient()
        val url = "https://api.github.com/users/$id"
        aClient.addHeader("Authorization", "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")
        aClient.addHeader("User-Agent", "request")
        aClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val user = UserData()
                    user.avatar = jsonObject.getString("avatar_url").toString()
                    user.username = jsonObject.getString("login")
                    user.name = jsonObject.getString("name")
                    user.company = jsonObject.getString("company").toString()
                    user.location = jsonObject.getString("location").toString()
                    user.repository = jsonObject.getString("public_repos")
                    user.followers = jsonObject.getString("followers")
                    user.following = jsonObject.getString("following")
                    list.add(user)
                    showRecyclerList()
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

}


