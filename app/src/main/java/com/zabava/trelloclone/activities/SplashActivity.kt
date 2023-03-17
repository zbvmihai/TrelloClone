package com.zabava.trelloclone.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.zabava.trelloclone.databinding.ActivitySplashBinding
import com.zabava.trelloclone.firebase.FirestoreClass

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var binding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeFace: Typeface = Typeface.createFromAsset(assets,"Poppins-Bold.ttf")
        binding?.tvSplashTitle?.typeface = typeFace

        Handler().postDelayed({

            val currentUserID = FirestoreClass().getCurrentUserId()

            if (currentUserID.isNotEmpty()){
                startActivity(Intent(this,MainActivity::class.java))
            }else{
                startActivity(Intent(this,IntroActivity::class.java))
            }
            finish()
        },2500)

    }
}