package com.zabava.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivityMainBinding
import com.zabava.trelloclone.databinding.ActivityMyProfileBinding
import com.zabava.trelloclone.firebase.FirestoreClass
import com.zabava.trelloclone.models.User

@Suppress("DEPRECATION")
class MyProfileActivity : BaseActivity() {

    private var binding: ActivityMyProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        setupActionBar()

        binding?.toolbarMyProfileActivity?.setNavigationOnClickListener { onBackPressed() }

        FirestoreClass().loadUserData(this)

    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarMyProfileActivity)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar?.title = resources.getString(R.string.my_profile)

    }

    fun setUserDataInUI(user: User){
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding?.ivUserImage!!)

        binding?.etName?.setText(user.name)
        binding?.etEmail?.setText(user.email)
        if (user.mobile != 0L){
            binding?.etMobile?.setText(user.mobile.toString())
        }
    }
}