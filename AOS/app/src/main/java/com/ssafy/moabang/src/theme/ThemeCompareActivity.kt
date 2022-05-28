package com.ssafy.moabang.src.theme

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ssafy.moabang.R
import com.ssafy.moabang.adapter.CompareThemeListRVAdapter
import com.ssafy.moabang.adapter.CompareTitleListRVAdapter
import com.ssafy.moabang.data.model.dto.ThemeForCompare
import com.ssafy.moabang.data.model.repository.Repository
import com.ssafy.moabang.databinding.ActivityThemeCompareBinding
import com.ssafy.moabang.src.util.CompareList
import com.ssafy.moabang.src.util.LocationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeCompareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThemeCompareBinding
    private lateinit var compareTitleListRVAdapter: CompareTitleListRVAdapter
    lateinit var compareThemeListRVAdapter: CompareThemeListRVAdapter
    private val labels = listOf("평점", "거리", "난이도", "활동성")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeCompareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.toolbarThemeCompare.ivToolbarIcon.setOnClickListener { finish() }
        binding.toolbarThemeCompare.tvToolbarTitle.text = "테마 비교"

        if(CompareList.items.isNotEmpty()) binding.tvTcbsBlank.visibility = View.GONE

        setRVAdapter()
        setChart()
        setData()
    }

    private fun setChart(){
        binding.barchartThemeCompare.apply{
            setDrawBarShadow(false)
            description.isEnabled = false
            setPinchZoom(false)
            setDrawGridBackground(false)
            legend.isEnabled = true
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.form = Legend.LegendForm.CIRCLE
            legend.textColor = R.color.moabang_gray
            axisRight.isEnabled = false
        }

        binding.barchartThemeCompare.xAxis.apply{
            setCenterAxisLabels(true)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(true)
            granularity = 1F
            textColor = R.color.moabang_gray
            textSize = 12F
            axisLineColor = R.color.moabang_pink
            axisMinimum = 0F
            valueFormatter = IndexAxisValueFormatter(labels)
            axisMaximum = labels.size.toFloat()

        }

        binding.barchartThemeCompare.axisLeft.apply{
            axisMinimum = 0F
        }
    }

    private fun getDistance(item: ThemeForCompare) : Float {
        var current = LocationUtil().getCurrentLocation(this)
        var dis = 0F
        if(current != null){
            dis = LocationUtil().getDistanceLatLngInKm(current!!.latitude, current!!.longitude, item.lat!!.toDouble(), item.lon!!.toDouble()).toFloat()
        }
        return dis
    }

    private fun getActive(item: ThemeForCompare) : Float {
        var active = 0F
        active = when(item.activity){
            "적음" -> 1F
            "보통" -> 2F
            "많음" -> 3F
            else -> 0F
        }
        return active
    }

    private fun setData(){
        var valOne = floatArrayOf(0F, 0F, 0F, 0F)
        var valTwo = floatArrayOf(0F, 0F, 0F, 0F)
        var valThree = floatArrayOf(0F, 0F, 0F, 0F)

        if(CompareList.clist.size >= 1){
            var item = CompareList.clist[0]
            valOne = floatArrayOf(item.grade.toFloat(), getDistance(item), item.difficulty.toFloat(), getActive(item))
        }
        if(CompareList.clist.size >= 2){
            var item = CompareList.clist[1]
            valTwo = floatArrayOf(item.grade.toFloat(), getDistance(item), item.difficulty.toFloat(), getActive(item))
        }
        if(CompareList.clist.size >= 3){
            var item = CompareList.clist[2]
            valThree = floatArrayOf(item.grade.toFloat(), getDistance(item), item.difficulty.toFloat(), getActive(item))
        }

        val barOne: ArrayList<BarEntry> = ArrayList()
        val barTwo: ArrayList<BarEntry> = ArrayList()
        val barThree: ArrayList<BarEntry> = ArrayList()
        for (i in 0..3) {
            barOne.add(BarEntry(i.toFloat(), valOne[i]))
            barTwo.add(BarEntry(i.toFloat(), valTwo[i]))
            barThree.add(BarEntry(i.toFloat(), valThree[i]))
        }

        var set1 = BarDataSet(barOne, "")
        set1.color = Color.parseColor("#FFC7C7")
        var set2 = BarDataSet(barTwo, "")
        set2.color = Color.parseColor("#C7EFFF")
        var set3 = BarDataSet(barThree, "")
        set3.color = Color.parseColor("#E1FFC7")

        if(CompareList.clist.size >= 1) set1.label = CompareList.clist[0].tname
        if(CompareList.clist.size >= 2) set2.label = CompareList.clist[1].tname
        if(CompareList.clist.size >= 3) set3.label = CompareList.clist[2].tname

        set1.isHighlightEnabled = false
        set2.isHighlightEnabled = false
        set3.isHighlightEnabled = false
        set1.setDrawValues(false)
        set2.setDrawValues(false)
        set3.setDrawValues(false)

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        dataSets.add(set2)
        dataSets.add(set3)
        val data = BarData(dataSets)
        data.barWidth = 0.2f

        binding.barchartThemeCompare.apply{
            setData(data)
            setScaleEnabled(false)
            setVisibleXRangeMaximum(6f)
            animateXY(500, 500)
            groupBars(0f, 0.4f,0f)
            invalidate()
        }
    }

    private fun setRVAdapter(){
        compareTitleListRVAdapter = CompareTitleListRVAdapter()
        compareTitleListRVAdapter.data = CompareList.items

        binding.rvTcbs.apply {
            layoutManager = LinearLayoutManager(this@ThemeCompareActivity, LinearLayoutManager.VERTICAL, false)
            adapter = compareTitleListRVAdapter
        }

        var behavior = BottomSheetBehavior.from(binding.themeCompareBottomSheet)
        binding.tvTcbs.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        compareThemeListRVAdapter = CompareThemeListRVAdapter()

        CompareList.clistLiveData.observe(this){
            compareThemeListRVAdapter.data = it as MutableList<ThemeForCompare>
            Log.d("THEME COMPARE TEST", "setRVAdapter: $it")
            compareThemeListRVAdapter.notifyDataSetChanged()
            setData()
        }

        binding.rvThemeCompare.apply {
            layoutManager = LinearLayoutManager(this@ThemeCompareActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = compareThemeListRVAdapter
        }

        compareTitleListRVAdapter.itemClickListener = object : CompareTitleListRVAdapter.ItemClickListener {
            override fun onClick(item: ThemeForCompare) {
                val result = CompareList.addTheme(item)
                if(result == 2){
                    Toast.makeText(this@ThemeCompareActivity, "이미 추가된 테마입니다.", Toast.LENGTH_SHORT).show()
                } else if(result == 3){
                    Toast.makeText(this@ThemeCompareActivity, "동시에 세 개의 테마만 비교할 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        compareThemeListRVAdapter.itemClickListener = object : CompareThemeListRVAdapter.ItemClickListener {
            override fun onClick(item: ThemeForCompare) {
                CoroutineScope(Dispatchers.Main).launch {
                    val theme = Repository.get().getTheme(item.tid)
                    startActivity(Intent(this@ThemeCompareActivity, ThemeDetailActivity::class.java)
                        .putExtra("theme", theme.tid))
                }
            }
        }

    }

    override fun onBackPressed() {
        var behavior = BottomSheetBehavior.from(binding.themeCompareBottomSheet)
        if(behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            super.onBackPressed()
        } else {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}