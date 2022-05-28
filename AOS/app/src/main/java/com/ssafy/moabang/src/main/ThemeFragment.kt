package com.ssafy.moabang.src.main

import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.moabang.adapter.ThemeListRVAdapter
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.databinding.FragmentThemeBinding
import com.ssafy.moabang.src.theme.ThemeDetailActivity
import com.ssafy.moabang.src.theme.ThemeFilterActivity
import android.text.Editable
import android.util.Log
import android.view.MenuInflater
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.ssafy.moabang.R
import com.ssafy.moabang.data.model.repository.Repository
import com.ssafy.moabang.data.model.response.DoneTidResponse
import com.ssafy.moabang.data.model.viewmodel.MyPageViewModel
import com.ssafy.moabang.data.model.viewmodel.ThemeViewModel
import com.ssafy.moabang.src.theme.ThemeFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ThemeFragment : Fragment() {
    private lateinit var binding: FragmentThemeBinding
    private lateinit var repository: Repository
    lateinit var themeListRVAdapter: ThemeListRVAdapter
    private val myPageViewModel: MyPageViewModel by viewModels()

    private lateinit var originalList: MutableList<Theme>
    private lateinit var filteredList: MutableList<Theme>
    private lateinit var doneList: List<DoneTidResponse>
    private var searchList = ArrayList<Theme>()
    private var doneThemeList = ArrayList<Theme>()
    private lateinit var tf: ThemeFilter

    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == 1){
            val intent = it.data
            val returnValue = intent!!.getParcelableExtra<ThemeFilter>("tf")
            if (returnValue != null) {
                tf = returnValue
            }
            filter(returnValue!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Repository.get()
    }

    override fun onResume() {
        myPageViewModel.getAllDoneTid()
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.Main).launch {
            originalList = repository.getAllTheme() as MutableList<Theme>
            themeListRVAdapter.filterList(originalList)
        }
        if(::tf.isInitialized) filter(tf)
        super.onViewStateRestored(savedInstanceState)
    }

    fun init(){
        themeListRVAdapter = ThemeListRVAdapter()
        themeListRVAdapter.notifyDataSetChanged()

        myPageViewModel.doneTidListLiveData.observe(this){
            doneList = it
        }

        CoroutineScope(Dispatchers.Main).launch {
            originalList = repository.getAllTheme() as MutableList<Theme>
            themeListRVAdapter.filterList(originalList)
        }

        binding.switchThemeF.setOnCheckedChangeListener(SwitchListener())

        binding.rvThemeF.apply{
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = themeListRVAdapter
        }

        themeListRVAdapter.itemClickListener = object : ThemeListRVAdapter.ItemClickListener {
            override fun onClick(item: Theme) {
                if(item != null){
                    val intent = Intent(requireActivity(), ThemeDetailActivity::class.java).putExtra("theme", item.tid)
                    activityResultLauncher.launch(intent)
                }
            }
        }

        // 필터
        binding.btnThemeFFilter.setOnClickListener{
            val intent = Intent(requireActivity(), ThemeFilterActivity()::class.java)
            activityResultLauncher.launch(intent)
        }

        // 정렬
        binding.btnThemeFSort.setOnClickListener {
            showPopup(binding.btnThemeFSort)
        }

        search()
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(requireContext(), v)
        val inflater: MenuInflater = popup.menuInflater
        var list = if(!::filteredList.isInitialized || filteredList.isEmpty()) originalList else filteredList
        inflater.inflate(R.menu.theme_sort_menu, popup.menu)
        popup.setOnMenuItemClickListener { it ->
            when(it.itemId){
                R.id.sort_by_rate_descending -> {
                    list = list.sortedByDescending { it.grade } as MutableList<Theme>
                    themeListRVAdapter.filterList((list))
                    filteredList = list
                    true
                }
                R.id.sort_by_rate_ascending -> {
                    list = list.sortedBy { it.grade } as MutableList<Theme>
                    themeListRVAdapter.filterList((list))
                    filteredList = list
                    true
                }
                R.id.sort_by_tname_descending -> {
                    list = list.sortedByDescending { it.tname } as MutableList<Theme>
                    themeListRVAdapter.filterList((list))
                    filteredList = list
                    true
                }
                R.id.sort_by_tname_ascending -> {
                    list = list.sortedBy { it.tname } as MutableList<Theme>
                    themeListRVAdapter.filterList((list))
                    filteredList = list
                    true
                }
                R.id.sort_by_cname_descending -> {
                    list = list.sortedByDescending { it.cname } as MutableList<Theme>
                    themeListRVAdapter.filterList((list))
                    filteredList = list
                    true
                }
                R.id.sort_by_cname_ascending -> {
                    list = list.sortedBy { it.cname } as MutableList<Theme>
                    themeListRVAdapter.filterList((list))
                    filteredList = list
                    true
                }
            else -> false
            }
        }
        popup.show()
    }



    private fun filter(tf: ThemeFilter){
        Log.d("FILTER TEST", "filter: island = ${tf.island}, si = ${tf.si}")

        if(tf.island != "전체" && tf.si.size == 0){
            when (tf.island) {
                "서울" -> {
                    resources.getStringArray(R.array.cafe_list_si_seoul).toCollection(tf.si)
                }
                "경기/인천" -> {
                    resources.getStringArray(R.array.cafe_list_si_gyeonggi_incheon).toCollection(tf.si)
                }
                "충청" -> {
                    resources.getStringArray(R.array.cafe_list_si_chungcheong).toCollection(tf.si)
                }
                "강원" -> {
                    resources.getStringArray(R.array.cafe_list_si_gangwon).toCollection(tf.si)
                }
                "경상" -> {
                    resources.getStringArray(R.array.cafe_list_si_gyeongsang).toCollection(tf.si)
                }
                "전라" -> {
                    resources.getStringArray(R.array.cafe_list_si_jeolla).toCollection(tf.si)
                }
                "제주" -> {
                    resources.getStringArray(R.array.cafe_list_si_jeju).toCollection(tf.si)
                }
            }
            Log.d("TOCOLLECTION TEST", "tf.si : ${tf.si}")
        }
        if(tf.genre.size == 0) resources.getStringArray(R.array.genre_list).toCollection(tf.genre)
        if(tf.type.size == 0) resources.getStringArray(R.array.type_list).toCollection(tf.type)
        if(tf.diff.size == 0){
            for(i in 1..5) tf.diff.add(i)
        }
        if(tf.active.size == 0) {
            resources.getStringArray(R.array.active_list).toCollection(tf.active)
            tf.active.add("")
        }

        var minp = 0
        var maxp = 0
        if(tf.player.size > 0){
            minp = tf.player[0]
            if(tf.player.size == 1){
                maxp = tf.player[0]
            } else {
                maxp = tf.player[tf.player.size - 1]
            }
            if(maxp == 5) maxp = 10
        } else {
            minp = 0
            maxp = 10
        }

        var plist = ArrayList<Int>()
        for(i in minp..maxp){
            plist.add(i)
        }

        CoroutineScope(Dispatchers.Main).launch {
            filteredList = if(tf.island == "전체"){
                repository.filterThemesNoArea(tf.genre, tf.type, plist, tf.diff, tf.active)
            } else {
                repository.filterThemes(tf.island, tf.si, tf.genre, tf.type, plist, tf.diff, tf.active)
            }
            themeListRVAdapter.filterList(filteredList)
            if(filteredList.isEmpty()) Toast.makeText(requireContext(), "조건에 해당하는 테마가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun search(){
        // 검색
        binding.etThemeFSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val searchText: String = binding.etThemeFSearch.text.toString()
                searchFilter(searchText)
            }
        })
    }

    fun searchFilter(searchText: String) {
        val list = if(!::filteredList.isInitialized || filteredList.isEmpty()) originalList else filteredList
        searchList = ArrayList<Theme>()
        for (item in list) {
            if (item.tname.lowercase().contains(searchText.lowercase())) {
                searchList.add(item)
            }
        }
        themeListRVAdapter.filterList(searchList)
    }

    inner class SwitchListener: CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(button: CompoundButton, isChecked: Boolean) {
            val list = if(!::filteredList.isInitialized || filteredList.isEmpty()) originalList else filteredList
            doneThemeList = ArrayList<Theme>()
            if(isChecked){
                for(item in list){
                    if(!doneList.contains(DoneTidResponse(item.tid))){
                        doneThemeList.add(item)
                    }
                }
                themeListRVAdapter.filterList(doneThemeList)

            } else {
                themeListRVAdapter.filterList(list)

            }
        }

    }


}