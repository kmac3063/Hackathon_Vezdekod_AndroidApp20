package com.example.androidapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidapp.databinding.ActivityMainBinding
import com.example.androidapp.model.CardInfo
import com.example.androidapp.model.DataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        DataModel.onAttach(this)

        initViews()

        val data = DataModel.getCardsFromSDCARD()
        Log.d("gog", data.toString())
        recyclerView.adapter = CardAdapter(data)
//        GlobalScope.launch {
//            withContext(Dispatchers.Default) {
//                withContext(Dispatchers.Main) {
//                }
//            }
//        }
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        DataModel.onDetach()
    }
    private fun initViews() {
        recyclerView = findViewById(R.id.card_recycler_view)
    }

    class CardAdapter(var list: List<CardInfo>): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
        class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
            var descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view)
            var errorDateTextView: TextView = itemView.findViewById(R.id.error_date_text_view)
            var finishDateTextView: TextView = itemView.findViewById(R.id.finish_date_text_view)
            var statusTextView: TextView = itemView.findViewById(R.id.status_text_view)

            fun bind(cardInfo: CardInfo) {
                titleTextView.text = cardInfo.extsysname
                descriptionTextView.text = cardInfo.description
                errorDateTextView.text = Utils.formatDate(cardInfo.isknownerrordate)
                finishDateTextView.text = Utils.formatDate((cardInfo.targetfinish))
                statusTextView.text = cardInfo.status
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_card_recycler, parent, false)
            return CardViewHolder(view)
        }

        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount() = list.size
    }
}