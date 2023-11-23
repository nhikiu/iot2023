package com.example.fire.data.dto.iot

 data class MyIoT(val coConcentration: Long? = 0L,
                  val fireDetected: Boolean? = false,
                  val humidity: String? = "",
                  val temperature: String? = "",
                  val time: String? = "")