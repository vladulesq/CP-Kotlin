package com.example.chessapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.chessapp.AddOrEditActivity
import com.example.chessapp.R
import com.example.chessapp.models.Player
import java.util.ArrayList

class PlayerRecyclerAdapter(playerList: List<Player>, internal var context: Context) : RecyclerView.Adapter<PlayerRecyclerAdapter.TaskViewHolder>() {

    private var playerList: List<Player> = ArrayList()
    init {
        this.playerList = playerList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_player, parent, false)
        return TaskViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val player = playerList[position]
        holder.name.text = player.name
        holder.elo.text = player.elo.toString()

        holder.itemView.setOnClickListener {
            val i = Intent(context, AddOrEditActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", player.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName) as TextView
        var elo: TextView = view.findViewById(R.id.tvElo) as TextView
    }

}