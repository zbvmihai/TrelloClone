package com.zabava.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivityCardDetailsBinding

class CardDetailsActivity : BaseActivity() {
    private var binding: ActivityCardDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
    }
}