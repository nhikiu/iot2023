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

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users")

        val demoRef = database.getReference(DEMO)

        // đo nồng độ khí CO: long
        demoRef.child(COCONCENTRATION).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Long::class.java)
                Log.e("TAG", "onDataChange: CoCon: $value", )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        // đo trạng thái fireDetected: boolean
        demoRef.child(FIREDETECTED).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Boolean::class.java)
                Log.e("TAG", "onDataChange: FireDetected: $value", )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        // đo trạng thái humidity: float
        demoRef.child(HUMIDITY).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java)?.toFloat() ?: 0
                binding.tvHumidity.text = "$value"
                Log.e("TAG", "onDataChange: Humidity: $value", )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        // đo nhiệt độ temperature: float
        demoRef.child(TEMPERATURE).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java)?.toFloat() ?: 0
                binding.tvTemperature.text = "$value"
                binding.progressCircularTemperature.progress = value.toInt()
                Log.e("TAG", "onDataChange: Temperature: $value", )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val senserRef = database.getReference(SENSOR)

        // Loa
        senserRef.child(BUZZER).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Boolean::class.java) ?: false
                binding.switchBuzzer.isChecked = value
                Log.e("TAG", "onDataChange: BUZZer: $value", )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        // LED
        senserRef.child(LED).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Boolean::class.java) ?: false
                binding.switchLed.isChecked = value
                Log.e("TAG", "onDataChange: LED: $value", )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.switchLed.setOnCheckedChangeListener { buttonView, isChecked ->
            val sensorData = mapOf(
                LED to isChecked
            )

            senserRef.updateChildren(sensorData)
        }

        binding.switchBuzzer.setOnCheckedChangeListener { buttonView, isChecked ->
            val sensorData = mapOf(
                BUZZER to isChecked
            )

            senserRef.updateChildren(sensorData)
        }
    }

    override fun observeViewModel() {
    }

    private fun addEvent() {

    }

}
