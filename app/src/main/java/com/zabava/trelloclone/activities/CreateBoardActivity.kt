package com.zabava.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivityCreateBoardBinding

@Suppress("DEPRECATION")
class CreateBoardActivity : AppCompatActivity() {

    private var binding: ActivityCreateBoardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        setupActionBar()

        binding?.toolbarCreateBoardActivity?.setNavigationOnClickListener { onBackPressed() }

    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarCreateBoardActivity)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar?.title = resources.getString(R.string.create_board_title)

    }
}