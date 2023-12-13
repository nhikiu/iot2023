package com.example.fire.ui.component.splash

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.fire.R
import com.example.fire.databinding.DialogDetectFireBinding
import com.example.fire.utils.viewPerformClick

class DialogDetectFire (context: Context, var listener: OnClickListener) :
    Dialog(context, R.style.CustomDialog) {

    interface OnClickListener {

        fun clickCancel()
        fun clickTurnOff()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogDetectFireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(context)
            .asGif()
            .load(R.raw.fire)
            .into(binding.ivFire)

        binding.tvTurnOff.viewPerformClick {
            listener.clickTurnOff()
            this@DialogDetectFire.dismiss()
        }
        binding.tvCancel.viewPerformClick {
            this@DialogDetectFire.dismiss()
            listener.clickCancel()
        }
    }
}