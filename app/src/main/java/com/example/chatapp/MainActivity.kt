package com.example.chatapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var userRecylerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mDbRef: DatabaseReference


    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Hello: ...")
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.keepSynced(true);

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecylerView = findViewById(R.id.recycler_view_all_users)
        userRecylerView.layoutManager = LinearLayoutManager(this)
        userRecylerView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              userList.clear()
                for(postSnapShot in snapshot.children)
              {
                  val currentUser = postSnapShot.getValue(User::class.java)
                  if(mAuth.currentUser?.uid != currentUser?.uid) {
                      userList.add(currentUser!!)
                  }
              }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        mAuth.currentUser?.let {  getUsername(it.uid) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if(item.itemId == R.id.logout)
        {
            //TODO: logic to logout
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }

   private fun getUsername(uid: String)
    {

        mDbRef.child("user").child(uid).child("username").get().addOnSuccessListener {
            Toast.makeText(this@MainActivity, "Its a toast!"+ it.value, Toast.LENGTH_SHORT).show()
            supportActionBar?.title = "Hello: "+it.value.toString()
        }
    }
}

