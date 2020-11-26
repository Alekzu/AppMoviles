package co.edu.unal.geoloc.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import co.edu.unal.geoloc.R

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
    fun moveToHome(view: View){
        val intent = Intent(this, HomeMap::class.java)
        startActivity(intent)
    }
}