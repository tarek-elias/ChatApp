package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var edtMsgBox: EditText
    private lateinit var imgSendBtn: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    var receiverRoom: String? = null //unique room for each peer (sender & receiver)
    var senderRoom: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val senderUid = FirebaseAuth.getInstance().uid
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid+receiverUid
        setTitle(name)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        messageRecyclerView = findViewById(R.id.chat_recycler_view)
        edtMsgBox = findViewById(R.id.edt_chat_msgbox)
        imgSendBtn = findViewById(R.id.img_chat_send_btn)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        //TODO: logic for loading and adding the data to recycler view
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for(postSnapShot in snapshot.children){
                    val message = postSnapShot.getValue(Message::class.java)
                    messageList.add(message!!)
                }
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                }

            })


        //TODO: adding the msg to Db
        imgSendBtn.setOnClickListener{
            val msg = edtMsgBox.text.toString()
            val messageObject = Message(msg, senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObject)
                .addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObject)
                }
            edtMsgBox.setText("")
        }
    }


}