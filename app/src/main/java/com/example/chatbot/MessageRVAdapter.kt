package com.example.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageRVAdapter(private val messageList:ArrayList<MessageRVmodel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view:View
        return if(viewType == 0){
            view = LayoutInflater.from(parent.context).inflate(R.layout.user_message_rv_item,parent,false)
            UserMessageViewHolder(view)
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.bot_message_rv_item,parent,false)
            BotMessageViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sender = messageList.get(position).sender
        when(sender){
            "user"->(holder as UserMessageViewHolder).userMessageTV.setText(messageList.get(position).message)
            "bot"->(holder as BotMessageViewHolder).botMessageTV.setText(messageList.get(position).message)
        }
    }

    class UserMessageViewHolder(item:View):RecyclerView.ViewHolder(item)
    {
        val userMessageTV:TextView = item.findViewById(R.id.idTVUser)
    }

    class BotMessageViewHolder(item:View):RecyclerView.ViewHolder(item)
    {
        val botMessageTV:TextView = item.findViewById(R.id.idTVBot)
    }

    override fun getItemViewType(position: Int): Int {
        return when(messageList.get(position).sender){
            "user"->0
            "bot"->1
            else->1
        }
    }
}