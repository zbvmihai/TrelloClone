package com.zabava.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivityMainBinding
import com.zabava.trelloclone.databinding.ActivityMyProfileBinding

@Suppress("DEPRECATION")
class MyProfileActivity : BaseActivity() {

    private var binding: ActivityMyProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        setupActionBar()

        binding?.toolbarMyProfileActivity?.setNavigationOnClickListener { onBackPressed() }

    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarMyProfileActivity)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar?.title = resources.getString(R.string.my_profile)

    }
}