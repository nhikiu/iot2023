package com.example.fire.ui.component.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.example.fire.*
import com.example.fire.data.dto.iot.FragmentData
import com.example.fire.databinding.SplashLayoutBinding
import com.example.fire.service.FirebaseService
import com.example.fire.ui.base.BaseActivity
import com.example.fire.ui.component.home.HomeFragment
import com.example.fire.ui.component.dashboard.DashBoardFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: SplashLayoutBinding
    private lateinit var fragmentList: List<FragmentData>


    override fun initViewBinding() {
        binding = SplashLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService(Intent(this, FirebaseService::class.java))

        fragmentList = listOf(
            FragmentData(
                HomeFragment(),
                getString(R.string.home),
                getString(R.string.home),
                R.drawable.ic_home
            ),
            FragmentData(
                DashBoardFragment(),
                getString(R.string.dashboard),
                getString(R.string.dashboard),
                R.drawable.ic_dashboard
            )
        )

        val fragmentStateAdapter = ViewPagerAdapter(this, fragmentList)

        binding.viewpager.adapter = fragmentStateAdapter
        binding.viewpager.offscreenPageLimit = 3
        binding.viewpager.isUserInputEnabled = false

        // connect tablayout, viewpager
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            run {
                // custom tablayout
                val customView =
                    LayoutInflater.from(tab.parent!!.context).inflate(R.layout.item_tab, null)
                val icon = customView.findViewById<ImageView>(R.id.iv_tabitem)
                val title = customView.findViewById<TextView>(R.id.tv_tabitem)
                icon.setImageResource(fragmentList[position].img)
                title.text = fragmentList[position].title
                tab.customView = customView
            }
        }.attach()
    }

    override fun observeViewModel() {
    }

    private fun addEvent() {

    }

}
