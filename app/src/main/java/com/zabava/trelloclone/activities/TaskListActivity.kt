package com.zabava.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {

    private var binding: ActivityTaskListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
    }
}