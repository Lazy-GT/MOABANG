package com.ssafy.moabang.src.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.moabang.R
import com.ssafy.moabang.adapter.DoneThemeListRVAdapter
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.response.DoneThemeResponse
import com.ssafy.moabang.data.model.viewmodel.MyPageViewModel
import com.ssafy.moabang.databinding.ActivityMyDoneBinding
import com.ssafy.moabang.src.theme.ThemeDetailActivity

class MyDoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyDoneBinding
    private lateinit var doneThemeListRVA : DoneThemeListRVAdapter
    private val mypageViewModel: MyPageViewModel by viewModels()

    private lateinit var list: List<DoneThemeResponse>
    private var searchList = ArrayList<DoneThemeResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.toolbarDone.ivToolbarIcon.setOnClickListener { finish() }
        binding.toolbarDone.tvToolbarTitle.text = "이용한 테마"

        mypageViewModel.getAllDoneTheme()
        doneThemeListRVA = DoneThemeListRVAdapter()

        mypageViewModel.doneThemeListLiveData.observe(this){
            list = it
            doneThemeListRVA.filterList(it)
        }

        binding.rvDone.apply{
            layoutManager = LinearLayoutManager(this@MyDoneActivity, LinearLayoutManager.VERTICAL, false)
            adapter = doneThemeListRVA
        }

        doneThemeListRVA.itemClickListener = object : DoneThemeListRVAdapter.ItemClickListener {
            override fun onClick(item: DoneThemeResponse) {
                if(item != null){
                    startActivity(Intent(this@MyDoneActivity, ThemeDetailActivity::class.java)
                        .putExtra("theme", item.tid))
                }
            }
        }

        // 정렬
        binding.btnDoneSort.setOnClickListener {
            showPopup(binding.btnDoneSort)
        }

        search()
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.done_theme_sort_menu, popup.menu)
        popup.setOnMenuItemClickListener { it ->
            when(it.itemId){
                R.id.sort_by_rate -> {
                    list = if(list != list.sortedByDescending { it.rating }) list.sortedByDescending { it.rating }
                    else list.sortedBy { it.rating }
                    doneThemeListRVA.filterList((list))
                    true
                }
                R.id.sort_by_tname -> {
                    list = if(list != list.sortedByDescending { it.tname }) list.sortedByDescending { it.tname }
                    else list.sortedBy { it.tname }
                    doneThemeListRVA.filterList((list))
                    true
                }
                R.id.sort_by_cname -> {
                    list = if(list != list.sortedByDescending { it.cname }) list.sortedByDescending { it.cname }
                    else list.sortedBy { it.cname }
                    doneThemeListRVA.filterList((list))
                    true
                }
                R.id.sort_by_playDate -> {
                    list = if(list != list.sortedByDescending { it.playDate }) list.sortedByDescending { it.playDate }
                    else list.sortedBy { it.playDate }
                    doneThemeListRVA.filterList((list))
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun search(){
        // 검색
        binding.etDoneSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val searchText: String = binding.etDoneSearch.text.toString()
                searchFilter(searchText)
            }
        })
    }

    fun searchFilter(searchText: String) {
        searchList = ArrayList<DoneThemeResponse>()
        for (item in list) {
            if (item.tname.lowercase().contains(searchText.lowercase())) {
                searchList.add(item)
            }
        }
        doneThemeListRVA.filterList(searchList)
    }
}