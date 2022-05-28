package com.ssafy.moabang.src.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.ssafy.moabang.data.model.dto.ReportRequest
import com.ssafy.moabang.data.model.repository.ReportRepository
import com.ssafy.moabang.databinding.ReportBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ReportDialog(var context: Context, var target_id: Int, var from: Int, var reason: String) {

    private val binding = ReportBinding.inflate(LayoutInflater.from(context))
    private val dialog = Dialog(context)
    private val reportRepository = ReportRepository()

    fun show() {
        initView()
        dialog.apply {
            show()
        }
    }

    private fun initView() {
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.apply {
            setContentView(binding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        binding.apply {
            reportTarget.text = "내용 : ${reason}"
            reportCancel.setOnClickListener {
                dialog.dismiss()
            }
            reportSubmit.setOnClickListener {
                sendReport()
                dialog.dismiss()
            }
        }
    }

    private fun sendReport() {
        val tempReportRequest = ReportRequest(from, binding.reportReason.text.toString(), target_id)
        CoroutineScope(Dispatchers.Main).launch {
            var result: Boolean? = null
            CoroutineScope(Dispatchers.IO).async {
                try {
                    result = reportRepository.createReport(tempReportRequest)?.body()!!
                } catch (e: Exception) {
                    Log.d("AAAAA", "무야호" + e.toString())
                }
            }.await()
            if (result == true) {
                Toast.makeText(context, "신고 완료.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "이미 신고했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}