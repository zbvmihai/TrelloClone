package com.zabava.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivityMembersBinding

class MembersActivity : AppCompatActivity() {

    private var binding : ActivityMembersBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMembersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
    }
}