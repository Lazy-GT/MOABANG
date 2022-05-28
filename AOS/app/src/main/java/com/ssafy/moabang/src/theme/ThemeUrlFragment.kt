package com.ssafy.moabang.src.theme

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.ssafy.moabang.databinding.FragmentThemeUrlBinding
import android.webkit.WebSettings
import androidx.activity.OnBackPressedCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior


class ThemeUrlFragment: Fragment() {
    private lateinit var binding: FragmentThemeUrlBinding
    private lateinit var callback: OnBackPressedCallback
    private lateinit var themeUrl: String
    private lateinit var themeDetailActivity: ThemeDetailActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        themeUrl = arguments?.getString("url").toString()
        binding = FragmentThemeUrlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        themeDetailActivity = context as ThemeDetailActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (themeDetailActivity.behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    if (binding.themeUrlFWebview.canGoBack()) {
                        binding.themeUrlFWebview.goBack()
                    } else {
                        themeDetailActivity.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                } else if (themeDetailActivity.behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    val fragmentManager = activity?.supportFragmentManager
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction().remove(this@ThemeUrlFragment)
                            .commit()
                        fragmentManager.popBackStack()
                    }
                    themeDetailActivity.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.themeUrlFWebview.apply {
            webViewClient = WebViewClient()
            loadUrl(themeUrl)
        }

        val ws: WebSettings = binding.themeUrlFWebview.settings
        ws.javaScriptEnabled = true
    }

}