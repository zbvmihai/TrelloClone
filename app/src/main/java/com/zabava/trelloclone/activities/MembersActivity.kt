package com.zabava.trelloclone.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zabava.trelloclone.R
import com.zabava.trelloclone.adapters.MembersAdapter
import com.zabava.trelloclone.databinding.ActivityMembersBinding
import com.zabava.trelloclone.firebase.FirestoreClass
import com.zabava.trelloclone.models.Board
import com.zabava.trelloclone.models.User
import com.zabava.trelloclone.utils.Constants

@Suppress("DEPRECATION")
class MembersActivity : BaseActivity() {

    private var binding : ActivityMembersBinding? = null

    private lateinit var mBoardDetails: Board

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMembersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        if (intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }

        setupActionBar()

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAssignedMembersListDetails(this,mBoardDetails.assignedTo)
    }

    fun setupMembersList(list: ArrayList<User>){
        hideProgressDialog()

        binding?.rvMembersList?.layoutManager = LinearLayoutManager(this)
        binding?.rvMembersList?.setHasFixedSize(true)

        val adapter = MembersAdapter(this, list)
        binding?.rvMembersList?.adapter = adapter
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarMembersActivity)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar?.title = resources.getString(R.string.members)

        binding?.toolbarMembersActivity?.setNavigationOnClickListener { onBackPressed() }
    }
}