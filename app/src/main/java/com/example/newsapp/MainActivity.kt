package com.example.newsapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayoutStates
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        recyclerView=findViewById(R.id.recyclerview)
        recyclerView.layoutManager=LinearLayoutManager(this)

        //calling the function to get data from api
        fetch()


    }
    private fun updateRecyclerView(newsList:ArrayList<News>){
        val adapter=NewsAdapter(newsList)
        recyclerView.adapter=adapter
        Log.d("ListLength",newsList.size.toString())
    }

    private fun fetch() {

        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.getTopHeadlines("in", "business", "6ff929afb7264747b52b38f4b12c3e6f")

        call.enqueue(object : Callback<ResponseBody> { // Change the response type to ResponseBody
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val rawJson = response.body()?.string() // Convert the response body to string
                    if (rawJson != null) {
                      val newsList= parseJsonResponse(rawJson)
                        updateRecyclerView(newsList)

                    }    //call the function for parsing

                } else {
                    Log.d("MainActivity", "Response Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("MainActivity", "Network Error: ${t.message}")
            }
        })


    }

    private fun parseJsonResponse(response:String):ArrayList<News>{
        val newsList=ArrayList<News>()
        try{

            val jsonObject=JSONObject(response)
            val articlesArray=jsonObject.getJSONArray("articles")

            for(i in 0 until articlesArray.length()){
                val articleObject = articlesArray.getJSONObject(i)
                val sourceObject=articleObject.getJSONObject("source")
                val name=sourceObject.getString("name")
                val url=articleObject.getString("url")
                val imageUrl=articleObject.getString("urlToImage")
                val description=articleObject.getString("description")
                val content=articleObject.getString("content")
                 newsList.add(News(name,imageUrl,url,description,content))
            }
        }catch(e:Exception){
            e.printStackTrace()
        }

        return newsList
    }

}