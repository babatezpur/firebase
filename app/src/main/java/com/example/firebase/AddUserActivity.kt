package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebase.databinding.ActivityAddUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserActivity : AppCompatActivity() {

    lateinit var addUserBinding : ActivityAddUserBinding

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference : DatabaseReference = database.reference.child("MyUsers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addUserBinding = ActivityAddUserBinding.inflate(layoutInflater)
        val view = addUserBinding.root
        setContentView(view)

        supportActionBar?.title = "Add User"

        addUserBinding.buttonAddUser.setOnClickListener {
            addUserToDatabase()
        }
    }

    fun addUserToDatabase(){
        val name :String = addUserBinding.editTextName.text.toString()
        val age : Int = addUserBinding.editTextAge.text.toString().toInt()
        val email : String = addUserBinding.editTextEmail.text.toString()

        val id:String = myReference.push().key.toString()

        val user = Users(id,name,age,email)

        //below is for task completion confirmation

        myReference.child(id).setValue(user).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                Toast.makeText(
                    applicationContext, "User added to the DB", Toast.LENGTH_SHORT
                ).show()

                finish()
            }else{
                Toast.makeText(
                    applicationContext, task.exception.toString() , Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}