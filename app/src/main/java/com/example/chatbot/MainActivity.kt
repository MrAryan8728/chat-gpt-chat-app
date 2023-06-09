package com.example.chatbot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var queryEdt:TextInputEditText
    private lateinit var messageRV:RecyclerView
    lateinit var messsageRVAdapter: MessageRVAdapter
    lateinit var messageList:ArrayList<MessageRVmodel>
    val url = "https://api.openai.com/v1/completions"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        queryEdt = findViewById(R.id.idETQuery)
        messageRV = findViewById(R.id.idRVMessages)
        messageList = ArrayList()
        messsageRVAdapter = MessageRVAdapter(messageList)
        val layoutManager = LinearLayoutManager(applicationContext)
        messageRV.layoutManager = layoutManager
        messageRV.adapter = messsageRVAdapter

        queryEdt.setOnEditorActionListener(TextView.OnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEND) {
                if(queryEdt.text.toString().isNotEmpty()){
                    messageList.add(MessageRVmodel(queryEdt.text.toString(),"user"))
                    messsageRVAdapter.notifyDataSetChanged()
                    getResponse(queryEdt.text.toString())
                }else{
                    Toast.makeText(this,"Query to Enter karo",Toast.LENGTH_SHORT).show()
                }
                return@OnEditorActionListener true
            }
            false
        })
    }
    private fun getResponse(query:String)
    {
        queryEdt.setText("")
        val queue:RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject:JSONObject? = JSONObject()
        jsonObject?.put("model","text-davinci-003")
        jsonObject?.put("prompt",query)
        jsonObject?.put("temperature",0)
        jsonObject?.put("max_tokens",100)
        jsonObject?.put("top_p",1)
        jsonObject?.put("frequency_penalty",0.0)
        jsonObject?.put("presence_penalty",0.0)

        val postRequest:JsonObjectRequest = object:JsonObjectRequest(Method.POST,url,jsonObject,
            Response.Listener{response->
                val responseMsg:String = response.getJSONArray("choices").getJSONObject(0).getString("text")
                messageList.add(MessageRVmodel(responseMsg,"bot"))
                messsageRVAdapter.notifyDataSetChanged()
        },Response.ErrorListener{
            Toast.makeText(applicationContext,"Fail to get Response",Toast.LENGTH_SHORT).show()
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val params:MutableMap<String,String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "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"
                return params
            }
        }
        postRequest.retryPolicy = object:RetryPolicy{
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            override fun retry(error: VolleyError?) {

            }
        }
        queue.add(postRequest)
    }
}