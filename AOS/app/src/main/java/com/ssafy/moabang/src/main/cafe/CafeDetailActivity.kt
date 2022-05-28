package com.ssafy.moabang.src.main.cafe

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ssafy.moabang.R
import com.ssafy.moabang.adapter.HorizontalThemeListRVAdapter
import com.ssafy.moabang.config.GlobalApplication
import com.ssafy.moabang.data.api.CafeApi
import com.ssafy.moabang.data.model.dto.Cafe
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.repository.Repository
import com.ssafy.moabang.databinding.ActivityCafeDetailBinding
import com.ssafy.moabang.src.theme.ThemeDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CafeDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityCafeDetailBinding
    private lateinit var themeList: List<Theme>
    private var themeListRVAdapter : HorizontalThemeListRVAdapter = HorizontalThemeListRVAdapter()
    private lateinit var cafe: Cafe
    private var repository = Repository.get()
    private var mMap: GoogleMap? = null
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCafeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.cafeDetailMap
        mapView.getMapAsync(this)

        intent.getParcelableExtra<Cafe>("cafe")?.let {
            cafe = it
            initView()
        } ?: findCafeWithTitle()

        if(mapView != null){
            mapView.onCreate(savedInstanceState)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    private fun findCafeWithTitle() {
        val cname = intent.getStringExtra("cname")
        CoroutineScope(Dispatchers.Main).launch {
            cafe = repository.getCafeByNameExactly(cname!!)
            initView()
        }
    }

    private fun getJWTtoken(): String {
        val sp = getSharedPreferences("moabang", MODE_PRIVATE)
        return sp.getString("moabangToken", "NO_JWT_TOKEN")!!
    }

    private fun initView() {
        binding.toolbarCafeDetail.ivToolbarIcon.setOnClickListener { finish() }
        binding.toolbarCafeDetail.tvToolbarTitle.text = cafe.cname
        binding.ivCafeUrl.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(cafe.url)))
        }

        // cafe
        Glide.with(this)
            .load(cafe.img)
            .centerCrop()
            .into(binding.ivCafeDetailFImg)
        binding.tvCafeDetailFCphone.text = cafe.cphone
        binding.tvCafeDetailFLocation.text = cafe.location
        binding.tvCafeDetailFTime.text = cafe.time

        // themes
        val service: CafeApi = GlobalApplication.retrofit.create(CafeApi::class.java)
        val jwtToken = getJWTtoken()
        service.getThemeByCid(jwtToken, cafe.cid).enqueue(object : Callback<List<Theme>> {
            override fun onResponse(call: Call<List<Theme>>, response: Response<List<Theme>>) {
                if (response.isSuccessful) {
                    val data: List<Theme> = response.body()!!
                    Log.d("AAAAA", "themeList : $data")
                    themeList = data
                    themeListRVAdapter.data = themeList
                    binding.rvCafeDetailFThemeByCafe.apply {
                        adapter = themeListRVAdapter
                        layoutManager = LinearLayoutManager(this@CafeDetailActivity, LinearLayoutManager.HORIZONTAL, false)
                        themeListRVAdapter.itemClickListener = object : HorizontalThemeListRVAdapter.ItemClickListener {
                            override fun onClick(item: Theme) {
                                Log.d("AAAAA", "CAFE : $cafe")
                                item.apply {
                                    cname = cafe.cname.toString()
                                    url = cafe.url.toString()
                                    island = cafe.island.toString()
                                    si = cafe.location.toString()
                                }
                                Log.d("AAAAA", "ITEM(THEME) : $item")
                                val intent = Intent(
                                    this@CafeDetailActivity,
                                    ThemeDetailActivity::class.java
                                ).putExtra("theme", item.tid)
                                startActivity(intent)
                            }
                        }
                    }
                    Log.d("AAAAA", "data : $data")
                } else {
                    Log.e("AAAAA", "실패!")
                }
            }

            override fun onFailure(call: Call<List<Theme>>, t: Throwable) {
                Log.d("AAAAA", "네트워크 오류2 : ${t.message}")
                Toast.makeText(this@CafeDetailActivity, "네트워크 오류2 : ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        checkPermission()
        mMap = googleMap

    }

    private fun checkPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                if(cafe.lat != "" && cafe.lon != "") {
                    var loc = LatLng(cafe.lat!!.toDouble(), cafe.lon!!.toDouble())
                    val icon = AppCompatResources.getDrawable(this@CafeDetailActivity, R.drawable.ic_marker)!!.toBitmap(100, 100, null)
                    var markerOptions = MarkerOptions()
                    markerOptions.position(loc)
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
                    mMap?.addMarker(markerOptions)
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.0F))
                }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@CafeDetailActivity, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("방탈출 카페를 보기위해 권한이 필요합니다.")
            .setDeniedMessage("[설정] 에서 위치 접근 권한을 부여해야만 사용이 가능합니다.")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .setGotoSettingButton(true)
            .setGotoSettingButtonText("[설정] 에서 위치 접근 권한을 허용 할 수 있습니다.")
            .check()
    }
}