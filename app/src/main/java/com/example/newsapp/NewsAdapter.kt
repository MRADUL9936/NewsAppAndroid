package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class NewsAdapter(private val items:ArrayList<News>): RecyclerView.Adapter<NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.news_item,parent,false)
       return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("item","item :${items.size}")
     return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.itemView.apply {
            Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.imageview)
            holder.descview.text = currentItem.desc
            holder.sourceview.text = "source:" + currentItem.source

            setOnClickListener{
                startCustomTab(context,currentItem.url) //Open the custom chrome tab
            }
        }
    }

    private fun startCustomTab(context: Context, url:String){


        
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(context, Uri.parse(url))
    }

}


class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     val sourceview: TextView =itemView.findViewById(R.id.source)
     val imageview:ImageView=itemView.findViewById(R.id.image)
     val descview:TextView=itemView.findViewById(R.id.desc)

}