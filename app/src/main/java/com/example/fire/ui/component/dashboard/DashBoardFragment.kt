package com.example.fire.ui. component.dashboard

import android.graphics.Color
import android.util.Log
import com.example.fire.DEMO
import com.example.fire.data.dto.iot.MyIoT
import com.example.fire.databinding.FragmentDashBoardBinding
import com.example.fire.ui.base.BaseFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : BaseFragment<FragmentDashBoardBinding>() {
    private lateinit var temperatureList: MutableList<Float>
    private lateinit var myIoTList: MutableList<MyIoT>
    private lateinit var entriesTemperature: MutableList<Entry>

    override fun getDataBinding() = FragmentDashBoardBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
        temperatureList = mutableListOf()
        myIoTList = mutableListOf()
        entriesTemperature = mutableListOf()
    }

    override fun initView() {
        super.initView()

        binding.chartTemperature.xAxis.setLabelCount(10, true)
        binding.chartTemperature.axisLeft.axisMinimum = 0f
        binding.chartTemperature.axisLeft.axisMaximum = 100f
        binding.chartTemperature.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chartTemperature.xAxis.granularity = 1f
        binding.chartTemperature.axisRight.isEnabled = false

        binding.chartHumidity.xAxis.setLabelCount(10, true)
        binding.chartHumidity.axisLeft.axisMinimum = 0f
        binding.chartHumidity.axisLeft.axisMaximum = 100f
        binding.chartHumidity.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chartHumidity.xAxis.granularity = 1f
        binding.chartHumidity.axisRight.isEnabled = false

        binding.chartCo.xAxis.setLabelCount(10, true)
        binding.chartCo.axisLeft.axisMinimum = 0f
        binding.chartCo.axisLeft.axisMaximum = 10000f
        binding.chartCo.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chartCo.xAxis.granularity = 1f
        binding.chartCo.axisRight.isEnabled = false
    }

    override fun initData() {
        super.initData()
        getTemperatureList()
    }

    private fun getTemperatureList() {
        val database = FirebaseDatabase.getInstance()

        val demoRef = database.getReference(DEMO)
        val query = demoRef.orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    myIoTList = mutableListOf()
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
//                            myIoTList.add(value)
//                        }
                        }
                    }
                    myIoTList = myIoTList.filter { it.time != null && it.time.isNotEmpty() && it.time.trim().lowercase() != "nan"
                            && it.temperature != null && it.temperature.isNotEmpty() && it.temperature.trim().lowercase() != "nan"
                            && it.humidity != null && it.humidity.isNotEmpty() && it.humidity.trim().lowercase() != "nan"
                            && it.coConcentration != null}.toMutableList()
                    myIoTList = myIoTList.filter {
                        ((it.temperature?.toFloat() ?: 0F) > 0F
                                && (it.humidity?.toFloat() ?: 0F) > 0F)
                    }.toMutableList()
                    setTemperatureChartView()
                    setHumidityChartView()
                    setCOChartView()
                } else {
                    Log.e("TAG", "onDataChange: Not exist", )
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

//    private fun getStartTimeOfCurrentDay() : Long{
//        val zoneIdVietnam = ZoneId.of("Asia/Ho_Chi_Minh")
//        val currentDateTimeVietnam = ZonedDateTime.now(zoneIdVietnam)
//        val startOfDayVietnam = currentDateTimeVietnam.toLocalDate().atStartOfDay(zoneIdVietnam)
//        val timestampVN = startOfDayVietnam.toEpochSecond()
//        return timestampVN
//    }

    private fun setTemperatureChartView() {
        val result = myIoTList.filter { it.time != null && it.time.isNotEmpty() && it.time.trim().lowercase() != "nan"
                && it.temperature != null && it.temperature.isNotEmpty() && it.temperature.trim().lowercase() != "nan"}
        val lastTenValues = if (result.size > 10) {
            result.subList(result.size - 10, result.size)
        } else {
            result
        }
        Log.e("TAG", "Temperatures: $lastTenValues", )

        val entries = lastTenValues.mapIndexed { index, item ->
            Entry(index.toFloat(), item.temperature!!.toFloat())
        }

        val dataSet = LineDataSet(entries, "Nhiệt độ")
        dataSet.color = Color.RED
        dataSet.setCircleColor(Color.RED)
        dataSet.circleRadius = 5f
        dataSet.lineWidth = 2f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawValues(false)
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.RED
        dataSet.setDrawCircles(true)

        val lineChart = binding.chartTemperature
        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.description.isEnabled = false
        lineChart.invalidate()

    }

    private fun setHumidityChartView() {
        val result = myIoTList.filter { it.time != null && it.time.isNotEmpty() && it.time.trim().lowercase() != "nan"
                && it.humidity != null&& it.humidity.isNotEmpty() && it.humidity.trim().lowercase() != "nan"}
        val lastTenValues = if (result.size > 10) {
            result.subList(result.size - 10, result.size)
        } else {
            result
        }
        Log.e("TAG", "Humidity: $lastTenValues", )

        val entries = lastTenValues.mapIndexed { index, item ->
            Entry(index.toFloat(), item.humidity!!.toFloat())
        }

        val dataSet = LineDataSet(entries, "Độ ẩm")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.BLUE)
        dataSet.circleRadius = 5f
        dataSet.lineWidth = 2f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawValues(false)
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.BLUE
        dataSet.setDrawCircles(true)

        val lineChart = binding.chartHumidity
        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.description.isEnabled = false
        lineChart.invalidate()
    }

    private fun setCOChartView() {
        Log.e("TAG", "List: $myIoTList", )
        val result = myIoTList.filter { it.time != null && it.time.isNotEmpty() && it.time.trim().lowercase() != "nan"
                && it.coConcentration != null && it.coConcentration > 0}
        Log.e("TAG", "setCOChartView: $result", )
        val lastTenValues = if (result.size > 10) {
            result.subList(result.size - 10, result.size)
        } else {
            result
        }
        Log.e("TAG", "CO: $lastTenValues", )

        val entries = lastTenValues.mapIndexed { index, item ->
            Entry(index.toFloat(), item.coConcentration!!.toFloat())
        }

        val dataSet = LineDataSet(entries, "Nồng độ khí CO")
        dataSet.color = Color.GREEN
        dataSet.setCircleColor(Color.GREEN)
        dataSet.circleRadius = 5f
        dataSet.lineWidth = 2f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawValues(false)
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.GREEN
        dataSet.setDrawCircles(true)

        val lineChart = binding.chartCo

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.description.isEnabled = false
        lineChart.invalidate()
    }

//    private fun getHourInVietnam(timestampString: String?): Int {
//        if (timestampString != null && timestampString.isNotEmpty()) {
//            val timestamp = Timestamp(timestampString.toLong() * 1000)
//            return timestamp.minutes
//        }
//        return 0
//    }
//
//    fun convertTimestampToTime(timestamp: Long): String {
//        // Chuyển timestamp thành đối tượng Instant
//        val instant = Instant.ofEpochSecond(timestamp)
//
//        // Định dạng đối tượng Instant thành chuỗi giờ:phút:giây
//        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault())
//        return formatter.format(instant)
//    }

}