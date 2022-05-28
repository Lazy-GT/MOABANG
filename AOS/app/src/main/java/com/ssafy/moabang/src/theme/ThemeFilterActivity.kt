package com.ssafy.moabang.src.theme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ssafy.moabang.R
import com.ssafy.moabang.databinding.DialogThemeFilterBinding
import com.ssafy.moabang.src.main.ThemeFragment

class ThemeFilterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: DialogThemeFilterBinding

    private var island = ""
    private lateinit var siList : ArrayList<String>
    private lateinit var genreList : ArrayList<String>
    private lateinit var typeList : ArrayList<String>
    private lateinit var playerList : ArrayList<Int>
    private lateinit var diffList : ArrayList<Int>
    private lateinit var activeList : ArrayList<String>
    private lateinit var tf : ThemeFilter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DialogThemeFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.toolbarThemeFilter.ivToolbarIcon.setOnClickListener { this.onBackPressed() }
        binding.toolbarThemeFilter.tvToolbarTitle.text = "테마 필터"
        val list = resources.getStringArray(R.array.cafe_list_island)

        binding.spThemeD.apply{
            adapter = ArrayAdapter<String>(this@ThemeFilterActivity, R.layout.spinner_text, list)
            onItemSelectedListener = this@ThemeFilterActivity
        }

        setChipGroup(binding.cgThemeDGenre, resources.getStringArray(R.array.genre_list))
        setChipGroup(binding.cgThemeDType, resources.getStringArray(R.array.type_list))
        setChipGroup(binding.cgThemeDPlayer, resources.getStringArray(R.array.player_list))
        setChipGroup(binding.cgThemeDDiff, resources.getStringArray(R.array.diff_list))
        setChipGroup(binding.cgThemeDActive, resources.getStringArray(R.array.active_list))

        initOnClickListener()
    }

    private fun initOnClickListener(){
        binding.tvThemeDOk.setOnClickListener {
            // 초기화
            siList = ArrayList<String>()
            genreList = ArrayList<String>()
            typeList = ArrayList<String>()
            playerList = ArrayList<Int>()
            diffList = ArrayList<Int>()
            activeList = ArrayList<String>()
            /////////

            for(idx in binding.cgThemeDArea.checkedChipIds){
                siList.add(binding.cgThemeDArea.findViewById<Chip>(idx).text.toString())
            }

            for(idx in binding.cgThemeDGenre.checkedChipIds){
                genreList.add(binding.cgThemeDGenre.findViewById<Chip>(idx).text.toString())
            }

            for(idx in binding.cgThemeDType.checkedChipIds){
                typeList.add(binding.cgThemeDType.findViewById<Chip>(idx).text.toString())
            }

            for(idx in binding.cgThemeDPlayer.checkedChipIds){
                when(binding.cgThemeDPlayer.findViewById<Chip>(idx).text.toString()) {
                    "1인" -> {playerList.add(1)}
                    "2인" -> {playerList.add(2)}
                    "3인" -> {playerList.add(3)}
                    "4인" -> {playerList.add(4)}
                    "5인 이상" -> {playerList.add(5)}

                }

            }

            for(idx in binding.cgThemeDDiff.checkedChipIds){
                diffList.add(binding.cgThemeDDiff.findViewById<Chip>(idx).text.toString().toInt())
            }

            for(idx in binding.cgThemeDActive.checkedChipIds){
                activeList.add(binding.cgThemeDActive.findViewById<Chip>(idx).text.toString())
            }

            tf = ThemeFilter(island, siList, genreList, typeList, playerList, diffList, activeList)
            val intent = Intent(this@ThemeFilterActivity, ThemeFragment::class.java)
                .putExtra("tf", tf)
            setResult(1, intent)
            finish()
        }

        binding.tvThemeDCancel.setOnClickListener {
            val intent = Intent(this@ThemeFilterActivity, ThemeFragment::class.java)
            setResult(0, intent)
            finish()
        }
    }


    private fun setChipGroup(res: ChipGroup, list: Array<String>){
        for(i in list.indices){
            if(res == binding.cgThemeDArea && i == 0) continue
            res.addView(Chip(this).apply {
                text = list[i]
                isCheckable = true
                isCheckedIconVisible = true
                setCheckedIconTintResource(R.color.moabang_pink)
                setChipBackgroundColorResource(R.color.white)
                setTextAppearanceResource(R.style.ChipTextStyle2)
            })
        }
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 == binding.spThemeD) {
            var strings: Array<String> = emptyArray()
            when (p0.getItemAtPosition(p2)) {
                "전체" -> {
                    strings = emptyArray()
                }
                "서울" -> {
                    strings = resources.getStringArray(R.array.cafe_list_si_seoul)
                }
                "경기/인천" -> {
                    strings = resources.getStringArray(R.array.cafe_list_si_gyeonggi_incheon)
                }
                "충청" -> {
                    strings = resources.getStringArray(R.array.cafe_list_si_chungcheong)
                }
                "강원" -> {
                    strings = resources.getStringArray(R.array.cafe_list_si_gangwon)
                }
                "경상" -> {
                    strings = resources.getStringArray(R.array.cafe_list_si_gyeongsang)
                }
                "전라" -> {
                    strings = resources.getStringArray(R.array.cafe_list_si_jeolla)
                }
                "제주" -> {
                    strings = resources.getStringArray(R.array.cafe_list_si_jeju)
                }
            }
            island = p0.getItemAtPosition(p2).toString()
            binding.cgThemeDArea.removeAllViews()
            setChipGroup(binding.cgThemeDArea, strings)
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}