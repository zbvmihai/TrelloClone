package com.zabava.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
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

    companion object {
        const val MY_PROFILE_REQUEST_CODE: Int = 11
    }

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()

        binding?.navView?.setNavigationItemSelectedListener(this)

        FirestoreClass().loadUserData(this)

        binding?.includeBar?.fabCreateBoard?.setOnClickListener {
            startActivity(Intent(this, CreateBoardActivity::class.java))
        }

    }

    fun updateNavigationUserDetails(user: User) {

        val ivUserProfile = findViewById<ImageView>(R.id.iv_user_profile_image)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(ivUserProfile)

        val tvUserName = findViewById<TextView>(R.id.tv_username)
        tvUserName.text = user.name

    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.includeBar?.toolbarMainActivity)
        binding?.includeBar?.toolbarMainActivity?.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        binding?.includeBar?.toolbarMainActivity?.setNavigationOnClickListener {

            if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                binding?.drawerLayout?.openDrawer(GravityCompat.START)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE) {
            FirestoreClass().loadUserData(this)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(
                    Intent(
                        this,
                        MyProfileActivity::class.java
                    ),
                    MY_PROFILE_REQUEST_CODE
                )
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