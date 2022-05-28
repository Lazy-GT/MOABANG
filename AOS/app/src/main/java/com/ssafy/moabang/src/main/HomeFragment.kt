package com.ssafy.moabang.src.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.gms.maps.model.LatLng
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ssafy.moabang.adapter.Latest5CommunityRVAdapter
import com.ssafy.moabang.adapter.NearCafeListRVAdapter
import com.ssafy.moabang.adapter.ThemeListRVAdapter
import com.ssafy.moabang.data.model.dto.Cafe
import com.ssafy.moabang.data.model.dto.Community
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.repository.CafeRepository
import com.ssafy.moabang.data.model.repository.CommunityRepository
import com.ssafy.moabang.data.model.repository.Repository
import com.ssafy.moabang.databinding.FragmentHomeBinding
import com.ssafy.moabang.src.main.cafe.CafeDetailActivity
import com.ssafy.moabang.src.main.community.CommunityDetailActivity
import com.ssafy.moabang.src.util.LocationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.ssafy.moabang.src.theme.ThemeDetailActivity
import kotlinx.coroutines.*
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var currentLocation: LatLng? = null

    private var repository = Repository.get()

    private var nearCafeListRVAdapter: NearCafeListRVAdapter = NearCafeListRVAdapter(listOf())
    private lateinit var nearCafeList: List<Cafe>

    private var hotThemeListRVAdapter: ThemeListRVAdapter = ThemeListRVAdapter()
    private var hotThemeList: List<Theme> = listOf()

    private val snapHelperForNearCafe = LinearSnapHelper()
    private val snapHelperForHotTheme = LinearSnapHelper()

    private var cafeRepository = CafeRepository()

    private lateinit var latest5 : List<Community>
    private lateinit var latest5CommunityRVAdapter: Latest5CommunityRVAdapter

    private var recruitRepository = CommunityRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setHotThemeList()
        setLatest5RecruitList()
    }

    private fun checkPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val seoul = LatLng(37.566535, 126.9779692)
                currentLocation = LocationUtil().getCurrentLocation(requireContext()) ?: LatLng(seoul.latitude, seoul.longitude)
                setNearCafeList()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(requireContext(), "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("주위의 방탈출 카페를 보기위해 권한이 필요합니다.")
            .setDeniedMessage("[설정] 에서 위치 접근 권한을 부여해야만 사용이 가능합니다.")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .setGotoSettingButton(true)
            .setGotoSettingButtonText("[설정] 에서 위치 접근 권한을 허용 할 수 있습니다.")
            .check()
    }

    private fun setNearCafeList() {
        if (currentLocation != null) {
            CoroutineScope(Dispatchers.Main).launch {
                var allCafeList = listOf<Cafe>()
                var tempNearCafeList = mutableListOf<Cafe>()
                CoroutineScope(Dispatchers.IO).async {
                    allCafeList = repository.getAllCafe()
                }.await()
                for (cafe in allCafeList) {
                    if (cafe.lat == null || cafe.lat == "" || cafe.lon == null || cafe.lon == "") {
                        continue
                    } else {
                        val distance = if(cafe.lat == "" || cafe.lon == "" ) 0.0
                        else {
                            LocationUtil().getDistanceLatLngInKm(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude,
                                cafe.lat!!.toDouble(),
                                cafe.lon!!.toDouble()
                            )
                        }

                        cafe.distance = distance
                        tempNearCafeList.add(cafe)
                    }
                }
                tempNearCafeList.sortBy { it.distance }
                tempNearCafeList = tempNearCafeList.subList(0, 6)
                nearCafeList = tempNearCafeList
                initNearCafeRCV()
            }
        } else {
            Log.d("AAAAA", "HOME FRAGMENT : currentLocation is null")
        }
    }

    private fun setHotThemeList() {
        var allThemeListWithLike = listOf<Theme>()
        var tempThemeListWithLike = mutableListOf<Theme>()
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                allThemeListWithLike = getAllThemeWithLike()
            }.await()
            for (theme in allThemeListWithLike) {
                if (theme.count == 0) {
                    continue
                } else {
                    tempThemeListWithLike.add(theme)
                }
            }
            tempThemeListWithLike.sortByDescending { it.count }
            if (tempThemeListWithLike.size > 6) {
                tempThemeListWithLike = tempThemeListWithLike.subList(0, 6)
            }
            hotThemeList = tempThemeListWithLike
            initHotThemeRCV()
        }
    }

    private fun setLatest5RecruitList() {
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                latest5 = getLatest5Community()
                Log.d("AAAAA", "setLatest5RecruitList: $latest5")
            }.await()
            initLatest5()
        }
    }

    private fun initNearCafeRCV() {
        if (nearCafeList.isNotEmpty()) {
            binding.tvHomeFHotThemeEmpty.visibility = View.GONE

            nearCafeListRVAdapter = NearCafeListRVAdapter(nearCafeList)
            nearCafeListRVAdapter.setItemClickListener(object :
                NearCafeListRVAdapter.CafeItemClickListener {
                override fun onClick(cafe: Cafe) {
                    val intent =
                        Intent(requireActivity(), CafeDetailActivity::class.java).putExtra(
                            "cafe",
                            cafe
                        )
                    startActivity(intent)
                }
            })
            binding.rvHomeFNearCafe.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = nearCafeListRVAdapter
                visibility = View.VISIBLE
            }
            snapHelperForNearCafe.attachToRecyclerView(binding.rvHomeFNearCafe)
        } else {
            binding.rvHomeFNearCafe.visibility = View.GONE
            binding.tvHomeFHotThemeEmpty.visibility = View.VISIBLE
        }
    }

    private fun initHotThemeRCV() {
        hotThemeListRVAdapter.apply {
            from = "HomeFragment"
            data = hotThemeList
            itemClickListener = object :
                ThemeListRVAdapter.ItemClickListener {
                override fun onClick(item: Theme) {
                    val intent =
                        Intent(requireActivity(), ThemeDetailActivity::class.java).putExtra(
                            "theme",
                            item.tid
                        )
                    startActivity(intent)
                }
            }
        }
        binding.rvHomeFHotTheme.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hotThemeListRVAdapter
        }
        snapHelperForHotTheme.attachToRecyclerView(binding.rvHomeFHotTheme)
    }


    private fun initLatest5() {
        if(latest5.isNotEmpty()){
            latest5CommunityRVAdapter = Latest5CommunityRVAdapter(latest5)
            latest5CommunityRVAdapter.itemClickListener = object : Latest5CommunityRVAdapter.ItemClickListener {
                override fun onClick(community: Community) {
                    val intent = Intent(requireActivity(), CommunityDetailActivity::class.java).putExtra("community", community).putExtra("mode","read")
                    startActivity(intent)
                }
            }
            binding.rvHomeFLatest5Community.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = latest5CommunityRVAdapter
            }
        }
    }

    private suspend fun getAllThemeWithLike(): List<Theme> = withContext(Dispatchers.IO) {
        val result: Response<List<Theme>>? = cafeRepository.getAllThemeWithLike()
        if (result != null) {
            return@withContext result.body()!!
        } else {
            Log.d("AAAAA", "HOME FRAGMENT_getAllThemeWithLike : null")
            return@withContext emptyList()
        }
    }

    private suspend fun getLatest5Community() : List<Community> = withContext(Dispatchers.IO) {
        val result: Response<List<Community>>? = recruitRepository.getAllCommunity()
        if (result != null) {
            val tempResult = result.body()?.filter { it.reportCnt <= 3 }?.take(5)
            return@withContext tempResult!!
        } else {
            Log.d("AAAAA", "HOME FRAGMENT_getLatest5Community : null")
            return@withContext emptyList()
        }
    }
}