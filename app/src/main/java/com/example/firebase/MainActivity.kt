package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding : ActivityMainBinding

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference : DatabaseReference = database.reference.child("MyUsers")

    val userList = ArrayList<Users>()
    lateinit var usersAdapter : UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root

        setContentView(view)

        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0
            ,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = usersAdapter.getUserId(viewHolder.adapterPosition)
                myReference.child(id).removeValue()
                Toast.makeText(applicationContext,"The user was deleted", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(mainBinding.recyclerView)

        retreiveDataFromDatabase()

    }

    fun retreiveDataFromDatabase(){
        myReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (eachUser in snapshot.children){
                    val user = eachUser.getValue(Users::class.java)

                    if(user != null){
                        println("userID : ${user.userID}")
                        println("userName : ${user.userName}")
                        println("userAge : ${user.userAge}")
                        println("userEmail : ${user.userEmail}")
                        println("*******************")

                        userList.add(user)
                    }

                    usersAdapter = UsersAdapter(this@MainActivity, userList)
                    mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                    mainBinding.recyclerView.adapter = usersAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}