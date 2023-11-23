package com.example.fire.ui.component.dashboard

import android.graphics.Color
import android.util.Log
import com.example.fire.DEMO
import com.example.fire.R
import com.example.fire.data.dto.iot.MyIoT
import com.example.fire.databinding.FragmentDashBoardBinding
import com.example.fire.ui.base.BaseFragment
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp
import java.time.*
import java.time.format.DateTimeFormatter

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

        binding.chartHumidity.xAxis.setLabelCount(10, true)
        binding.chartHumidity.axisLeft.axisMinimum = 0f
        binding.chartHumidity.axisLeft.axisMaximum = 100f
        binding.chartHumidity.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chartHumidity.xAxis.granularity = 1f

        binding.chartCo.xAxis.setLabelCount(10, true)
        binding.chartCo.axisLeft.axisMinimum = 0f
        binding.chartCo.axisLeft.axisMaximum = 100f
        binding.chartCo.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chartCo.xAxis.granularity = 1f
    }

    override fun initData() {
        super.initData()
        Log.e("TAG", "Start time of current day: ${getStartTimeOfCurrentDay()}", )

        getTemperatureList()
    }

    private fun getTemperatureList() {
        val database = FirebaseDatabase.getInstance()

        val demoRef = database.getReference(DEMO)
        val query = demoRef.orderByKey().limitToLast(20)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val key = childSnapshot.key
                        val value = childSnapshot.getValue(MyIoT::class.java)

                        // Xử lý dữ liệu tại đây, ví dụ in ra màn hình
                        if (value != null) {
                            myIoTList.add(value)
                        }

                    }
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

    private fun getStartTimeOfCurrentDay() : Long{
        // Lấy thời gian hiện tại ở múi giờ Việt Nam
        val zoneIdVietnam = ZoneId.of("Asia/Ho_Chi_Minh")
        val currentDateTimeVietnam = ZonedDateTime.now(zoneIdVietnam)

        // Chuyển đổi thành thời gian bắt đầu của ngày
        val startOfDayVietnam = currentDateTimeVietnam.toLocalDate().atStartOfDay(zoneIdVietnam)

        // Chuyển đổi thành timestamp (giây từ epoch)
        val timestampVN = startOfDayVietnam.toEpochSecond()

        Log.d("TAG", "Current time vietname: $timestampVN")

        // Chuyển đổi ngày thành thời điểm bắt đầu của ngày (00:00:00)
        Log.e("TAG", "getStartTimeOfCurrentDay: ${System.currentTimeMillis()/1000}", )

        // Chuyển đổi thời điểm thành timestamp (giây từ epoch)
        return timestampVN
    }

    private fun setTemperatureChartView() {
        val result = myIoTList.filter { it.time != null && it.time.isNotEmpty()  && it.temperature != null&& it.temperature.isNotEmpty()}
        val lastTenValues = if (result.size > 10) {
            result.subList(result.size - 10, result.size)
        } else {
            result
        }

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
        lineChart.isEnabled = false
        lineChart.invalidate()
    }

    private fun setHumidityChartView() {
        val result = myIoTList.filter { it.time != null && it.time.isNotEmpty()  && it.humidity != null&& it.humidity.isNotEmpty()}
        val lastTenValues = if (result.size > 10) {
            result.subList(result.size - 10, result.size)
        } else {
            result
        }

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
        val result = myIoTList.filter { it.time != null && it.time.isNotEmpty() && it.coConcentration != null}
        val lastTenValues = if (result.size > 10) {
            result.subList(result.size - 10, result.size)
        } else {
            result
        }

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



    private fun getHourInVietnam(timestampString: String?): Int {
        if (timestampString != null && timestampString.isNotEmpty()) {
            val timestamp = Timestamp(timestampString.toLong() * 1000)
            return timestamp.minutes
        }
        return 0
    }

    fun convertTimestampToTime(timestamp: Long): String {
        // Chuyển timestamp thành đối tượng Instant
        val instant = Instant.ofEpochSecond(timestamp)

        // Định dạng đối tượng Instant thành chuỗi giờ:phút:giây
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

}