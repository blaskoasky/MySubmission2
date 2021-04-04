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



// <<< INI KOPAS >>>
class MainActivity : AppCompatActivity() {

    private var listData: ArrayList<UserData> = ArrayList()
    private lateinit var adapter: ListUserAdapter

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Github User's Search"

        adapter = ListUserAdapter(listData)

        recyclerViewConfig()
        searchUser()
        getUser()
    }

    private fun searchUser() { search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isEmpty()) {
                return true
            } else {
                listData.clear()
                getUserSearch(query)
            }
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    })
    }

    private fun getUser() {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    private fun getUserSearch(id: String) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    private fun getUserDetail(id: String) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val username: String? = jsonObject.getString("login").toString()
                    val name: String? = jsonObject.getString("name").toString()
                    val avatar: String? = jsonObject.getString("avatar_url").toString()
                    val company: String? = jsonObject.getString("company").toString()
                    val location: String? = jsonObject.getString("location").toString()
                    val repository: String? = jsonObject.getString("public_repos")
                    val followers: String? = jsonObject.getString("followers")
                    val following: String? = jsonObject.getString("following")
                    listData.add(
                            UserData(
                                    username,
                                    name,
                                    avatar,
                                    company,
                                    location,
                                    repository,
                                    followers,
                                    following
                            )
                    )
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    private fun recyclerViewConfig() {
        rv_main.layoutManager = LinearLayoutManager(rv_main.context)
        rv_main.setHasFixedSize(true)
        rv_main.addItemDecoration(
                DividerItemDecoration(
                        rv_main.context,
                        DividerItemDecoration.VERTICAL
                )
        )
    }

    private fun showRecyclerList() {
        rv_main.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = ListUserAdapter(listData)
        rv_main.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(dataUsers: UserData) {
                showSelectedUser(dataUsers)
            }
        })
    }

    private fun showSelectedUser(dataUser: UserData) {
        UserData(
                dataUser.username,
                dataUser.name,
                dataUser.avatar,
                dataUser.company,
                dataUser.location,
                dataUser.repository,
                dataUser.followers,
                dataUser.following
        )
        val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_DATA, dataUser)

        this@MainActivity.startActivity(intent)
        Toast.makeText(
                this,
                "${dataUser.name}",
                Toast.LENGTH_SHORT
        ).show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }*/
}


// <<<ISI SEBLEUM KOPAS >>>

/*

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

    }

    private fun searchUser() {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isEmpty()){
                    return true
                } else {
                    list.clear()
                    getUserSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }





    private fun showRecycleList(){
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
        aClient.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
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
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setUser(userID: String?) {
        binding.progressBar.visibility = View.VISIBLE

        val aClient = AsyncHttpClient()
        val url = "https://api.github.com/users/$userID"
        aClient.addHeader("Authorization", "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")
        aClient.addHeader("User-Agent", "request")
        aClient.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val user = UserData()
                    user.avatar = jsonObject.getString("avatar_url").toString()
                    user.username = jsonObject.getString("login")
                    user.name = jsonObject.getString("name")
                    user.repository = jsonObject.getString("public_repos")
                    user.company = jsonObject.getString("company")
                    user.location = jsonObject.getString("location")
                    user.followers = jsonObject.getString("followers")
                    user.following = jsonObject.getString("following")
                    list.add(user)
                    showRecycleList()
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val eMessage = when (statusCode){
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Forbidden"
                    404 -> "$statusCode: Not Found"
                    else -> "$statusCode: ${error.message}"
                }
                Toast.makeText(this@MainActivity, eMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUserSearch(id: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        setUser(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    private fun searchUser() {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    list.clear()
                    getUserSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}
*/
