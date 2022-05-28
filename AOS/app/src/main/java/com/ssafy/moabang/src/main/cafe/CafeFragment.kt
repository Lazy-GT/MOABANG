package com.ssafy.moabang.src.main.cafe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.ssafy.moabang.R
import com.ssafy.moabang.databinding.FragmentCafeBinding


class CafeFragment : Fragment() {
    private lateinit var binding: FragmentCafeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCafeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingTabLayout()
    }

    private fun settingTabLayout(){
        parentFragmentManager.beginTransaction().replace(R.id.container, CafeListFragment()).commit()

        binding.cafeTabLaout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0 -> parentFragmentManager.beginTransaction()
                        .replace(R.id.container, CafeListFragment())
                        .addToBackStack(null)
                        .commit()

                    1 -> parentFragmentManager.beginTransaction()
                        .replace(R.id.container, CafeMapFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
}