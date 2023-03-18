package com.zabava.trelloclone.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivityMainBinding
import com.zabava.trelloclone.firebase.FirestoreClass
import com.zabava.trelloclone.models.User

@Suppress("DEPRECATION")
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()

        binding?.navView?.setNavigationItemSelectedListener(this)

        FirestoreClass().signInUser(this)

    }

    fun updateNavigationUserDetails(user: User){

        val ivUserProfile = findViewById<ImageView>(R.id.iv_user_image)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(ivUserProfile)

        val tvUserName = findViewById<TextView>(R.id.tv_username)
        tvUserName.text = user.name

    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.includeBar?.toolbarMainActivity)
        binding?.includeBar?.toolbarMainActivity?.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        binding?.includeBar?.toolbarMainActivity?.setNavigationOnClickListener {

            if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!){
                binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            }else{
                binding?.drawerLayout?.openDrawer(GravityCompat.START)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!){
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile -> {
                startActivity(Intent(this, MyProfileActivity::class.java))
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)

        return true
    }
}