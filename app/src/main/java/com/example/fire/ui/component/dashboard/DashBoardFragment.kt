package com.example.fire.ui.component.dashboard

import com.example.fire.databinding.FragmentDashBoardBinding
import com.example.fire.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : BaseFragment<FragmentDashBoardBinding>() {
    override fun getDataBinding() = FragmentDashBoardBinding.inflate(layoutInflater)

    override fun initView() {
        super.initView()

    }
}