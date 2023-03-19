package com.zabava.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.zabava.trelloclone.R
import com.zabava.trelloclone.adapters.BoardItemsAdapter
import com.zabava.trelloclone.databinding.ActivityMainBinding
import com.zabava.trelloclone.firebase.FirestoreClass
import com.zabava.trelloclone.models.Board
import com.zabava.trelloclone.models.User
import com.zabava.trelloclone.utils.Constants

@Suppress("DEPRECATION")
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mUserName: String

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()

        binding?.navView?.setNavigationItemSelectedListener(this)

        FirestoreClass().loadUserData(this, true)

        binding?.includeBar?.fabCreateBoard?.setOnClickListener {

            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivityForResult(intent,Constants.CREATE_BOARD_REQUEST_CODE)
        }
    }

    fun updateNavigationUserDetails(user: User, readBoardList: Boolean) {

        mUserName = user.name

        val ivUserProfile = findViewById<ImageView>(R.id.iv_user_profile_image)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(ivUserProfile)

        val tvUserName = findViewById<TextView>(R.id.tv_username)
        tvUserName.text = user.name

        if (readBoardList){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardsList(this)
        }
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
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.MY_PROFILE_REQUEST_CODE) {
            FirestoreClass().loadUserData(this)
        }else if(resultCode == Activity.RESULT_OK && requestCode == Constants.CREATE_BOARD_REQUEST_CODE){
            FirestoreClass().getBoardsList(this)

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
                    Constants.MY_PROFILE_REQUEST_CODE
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

    fun populateBoardListToUI(boardsList: ArrayList<Board>){
        hideProgressDialog()

        if (boardsList.size >0){
            binding?.includeBar?.mainContent?.rvBoardsList?.visibility = View.VISIBLE
            binding?.includeBar?.mainContent?.tvNoBoardsAvailable?.visibility = View.GONE

            binding?.includeBar?.mainContent?.rvBoardsList?.layoutManager = LinearLayoutManager(this)
            binding?.includeBar?.mainContent?.rvBoardsList?.setHasFixedSize(true)

            val adapter = BoardItemsAdapter(this, boardsList)
            binding?.includeBar?.mainContent?.rvBoardsList?.adapter = adapter

            adapter.setOnClickListener(object: BoardItemsAdapter.OnClickListener{
                override fun onClick(position: Int, model: Board) {
                    var intent = Intent(this@MainActivity,TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }
            })
        }else{
            binding?.includeBar?.mainContent?.rvBoardsList?.visibility = View.GONE
            binding?.includeBar?.mainContent?.tvNoBoardsAvailable?.visibility = View.VISIBLE
        }
    }
}