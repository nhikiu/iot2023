package com.example.fire.ui.component.home

import android.app.ProgressDialog.show
import android.util.Log
import com.bumptech.glide.Glide
import com.example.fire.*
import com.example.fire.data.dto.iot.MyIoT
import com.example.fire.databinding.DialogDetectFireBinding
import com.example.fire.databinding.FragmentHomeBinding
import com.example.fire.ui.base.BaseFragment
import com.example.fire.ui.component.splash.DialogDetectFire
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>() {
    private lateinit var myIoTList: MutableList<MyIoT>
    private var isOnLed: Boolean = false
    private var isOnBuzzer: Boolean = false
    private var dialogDetectFire: DialogDetectFire? = null

    override fun getDataBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
        myIoTList = mutableListOf()
    }

    override fun initView() {
        super.initView()
        dialogDetectFire = DialogDetectFire(requireContext(), object : DialogDetectFire.OnClickListener {
            override fun clickCancel() {
            }

            override fun clickTurnOff() {
                onClickLED()
                onClickBuzzer()
            }
        })
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
//                        val key = childSnapshot.key
                        val value = childSnapshot.getValue(MyIoT::class.java)
                        val dataMap = childSnapshot.value as? Map<String, Any>
                        if (dataMap != null) {
                            val coConcentration = dataMap["coConcentration"] as Long?
                            val fireDetected = dataMap["fireDetected"] as Boolean?
                            val gas = dataMap["gas"] as Long?
                            val humidity = dataMap["humidity"] as String?
                            val temperature = dataMap["temperature"] as String?
                            myIoTList.add(MyIoT(coConcentration, fireDetected, humidity, temperature, childSnapshot.key, gas))

//                            if (value != null) {
//                              myIoTList.add(value)
//                            }
                        }
                    }
                    myIoTList.filter { it.coConcentration != null && it.humidity != null && it.humidity.isNotEmpty() && it.humidity.trim().lowercase() != "nan"
                            && it.time != null && it.time.isNotEmpty() && it.time.trim().lowercase() != "nan"
                            && it.temperature != null && it.temperature.isNotEmpty() && it.temperature.trim().lowercase() != "nan"}
                    val myIoT = myIoTList.last()
                    val temperature = myIoT.temperature?.toFloatOrNull() ?: 0.0F
                    val humidity = myIoT.humidity?.toFloatOrNull()?.toInt() ?: 0
                    if (temperature > 0 && humidity > 0) {
                        binding.tvTemperature.text = "$temperature"
                        binding.tvHumidity.text = "$humidity%"
                        binding.progressCircularTemperature.progress = temperature.toInt()
                        binding.progressCircularHumidity.progress = humidity
                    }
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
                isOnBuzzer = value
                checkBuzzerOnOff()
                checkDetectFire()
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
                checkDetectFire()
                Log.e("TAG", "onDataChange: LED: $value", )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.ivOnOffLed.setOnClickListener {
            onClickLED()
        }

        binding.ivOnOffBuzzer.setOnClickListener {
            onClickBuzzer()
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

    private fun checkDetectFire() {
        if (isOnBuzzer && isOnLed) {
            binding.tvDetect.text = resources.getString(R.string.yes)
            if (dialogDetectFire != null && !dialogDetectFire!!.isShowing)
            dialogDetectFire?.apply {
                setOnDismissListener {}
                show()
            }
        } else {
            binding.tvDetect.text = resources.getString(R.string.no)
        }
    }

    private fun onClickLED() {
        val database = FirebaseDatabase.getInstance()
        val senserRef = database.getReference(SENSOR)

        isOnLed = !isOnLed
        val sensorData = mapOf(
            LED to isOnLed
        )

        senserRef.updateChildren(sensorData)

        checkLedOnOff()
    }

    private fun onClickBuzzer() {
        val database = FirebaseDatabase.getInstance()
        val senserRef = database.getReference(SENSOR)

        isOnBuzzer = !isOnBuzzer
        val sensorData = mapOf(
            BUZZER to isOnBuzzer
        )

        senserRef.updateChildren(sensorData)

        checkBuzzerOnOff()
    }
}