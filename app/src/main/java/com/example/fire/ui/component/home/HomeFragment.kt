package com.example.fire.ui.component.home

import android.util.Log
import com.example.fire.*
import com.example.fire.data.dto.iot.MyIoT
import com.example.fire.databinding.FragmentHomeBinding
import com.example.fire.ui.base.BaseFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>() {
    private lateinit var myIoTList: MutableList<MyIoT>
    private var isOnLed: Boolean = false
    private var isOnBuzzer = false

    override fun getDataBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
        myIoTList = mutableListOf()
    }

    override fun initData() {
        super.initData()
        val database = FirebaseDatabase.getInstance()

        val demoRef = database.getReference(DEMO)
        val query = demoRef.orderByKey().limitToLast(10)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val key = childSnapshot.key
                        val value = childSnapshot.getValue(MyIoT::class.java)

                        if (value != null) {
                            myIoTList.add(value)
                        }

                    }
                    myIoTList.filter { it.coConcentration != null && it.humidity != null && it.humidity.isNotEmpty() && it.humidity.trim().lowercase() != "nan"
                            && it.time != null && it.time.isNotEmpty() && it.time.trim().lowercase() != "nan"
                            && it.temperature != null && it.temperature.isNotEmpty() && it.temperature.trim().lowercase() != "nan"}
                    val myIoT = myIoTList.last()
                    val temperature = myIoT.temperature?.toFloatOrNull()?.toInt() ?: 0
                    val humidity = myIoT.humidity?.toFloatOrNull()?.toInt() ?: 0
                    binding.tvTemperature.text = "$temperature"
                    binding.tvHumidity.text = "$humidity%"
                    binding.progressCircularTemperature.progress = temperature
                    binding.progressCircularHumidity.progress = humidity
                } else {
                    Log.e("TAG", "onDataChange: Not exist",)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


            // đo nồng độ khí CO: long
//        demoRef.child(COCONCENTRATION).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val value = snapshot.getValue(Long::class.java)
//                Log.e("TAG", "onDataChange: CoCon: $value", )
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//
//        // đo trạng thái fireDetected: boolean
//        demoRef.child(FIREDETECTED).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val value = snapshot.getValue(Boolean::class.java)
//                Log.e("TAG", "onDataChange: FireDetected: $value", )
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//
        val senserRef = database.getReference(SENSOR)

        // Loa
        senserRef.child(BUZZER).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Boolean::class.java) ?: false
                checkBuzzerOnOff()
                Log.e("TAG", "onDataChange: BUZZer: $value", )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        // LED
        senserRef.child(LED).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Boolean::class.java) ?: false
                isOnLed = value
                checkLedOnOff()
                Log.e("TAG", "onDataChange: LED: $value", )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.ivOnOffLed.setOnClickListener {
            isOnLed = !isOnLed
            val sensorData = mapOf(
                LED to isOnLed
            )

            senserRef.updateChildren(sensorData)

            checkLedOnOff()
        }

        binding.ivOnOffBuzzer.setOnClickListener {
            isOnBuzzer = !isOnBuzzer
            val sensorData = mapOf(
                BUZZER to isOnBuzzer
            )

            senserRef.updateChildren(sensorData)

            checkBuzzerOnOff()
        }
    }

    private fun checkLedOnOff() {
        if (isOnLed) {
            binding.ivOnOffLed.setImageResource(R.drawable.ic_on)
        } else {
            binding.ivOnOffLed.setImageResource(R.drawable.ic_off)
        }
    }

    private fun checkBuzzerOnOff() {
        if (isOnBuzzer) {
            binding.ivOnOffBuzzer.setImageResource(R.drawable.ic_on)
        } else {
            binding.ivOnOffBuzzer.setImageResource(R.drawable.ic_off)
        }
    }

    override fun addEvent() {
        super.addEvent()

    }
}