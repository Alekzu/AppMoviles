package co.edu.unal.geoloc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import co.edu.unal.geoloc.ui.login.LoginActivity
import co.edu.unal.geoloc.ui.login.Register

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)//reset theme
        setContentView(R.layout.activity_main)

    }

    fun moveToLogIn(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun moveToRegister(view: View){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

}