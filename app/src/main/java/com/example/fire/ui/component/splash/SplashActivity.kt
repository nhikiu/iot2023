package com.example.fire.ui.component.splash

import android.os.Bundle
import android.util.Log
import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import com.example.fire.*
import com.example.fire.databinding.SplashLayoutBinding
import com.example.fire.ui.base.BaseActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

/**
 * Created by TruyenIT
 */
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: SplashLayoutBinding

    override fun initViewBinding() {
        binding = SplashLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun observeViewModel() {
    }

    private fun addEvent() {

    }

}
