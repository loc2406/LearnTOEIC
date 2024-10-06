package com.locnguyen.toeicexercises.fragment

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.UpgradeBenefitsAdapter
import com.locnguyen.toeicexercises.adapter.UpgradeEvaluatesAdapter
import com.locnguyen.toeicexercises.databinding.UpgradeFragmentBinding
import com.locnguyen.toeicexercises.model.Evaluate
import com.locnguyen.toeicexercises.utils.toastMessage
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class UpgradeFragment: Fragment(), Runnable {

    private lateinit var binding: UpgradeFragmentBinding
    private lateinit var benefitAdapter: UpgradeBenefitsAdapter
    private lateinit var evaluateAdapter: UpgradeEvaluatesAdapter

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val pageSnapHelper: PagerSnapHelper by lazy { PagerSnapHelper() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UpgradeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        benefitAdapter = UpgradeBenefitsAdapter(listOf(
            Pair("Mở khóa bài học", "Tiếp cận nhiều đề kiểm tra hơn để nâng cao trình độ"),
            Pair("Không quảng cáo", "Giúp tối ưu trải nghiệm ứng dụng"),
            Pair("Học ngoại tuyến", "Học tập mọi lúc mọi nơi kể cả khi không có mạng")
        ))

        evaluateAdapter = UpgradeEvaluatesAdapter(listOf(
            Evaluate("Nguyễn Thị Xuân", "17:29 - 23/1/2023", 5, "Ứng dụng đã giúp mình cải thiện tiếng anh rất nhiều từ vốn kiến thức đến kĩ năng làm bài!",""),
            Evaluate("Nguyễn Thành Nam", "10:00 - 2/12/2023", 4, "Các câu hỏi trong phần luyện tập và kiểm tra bám sát đề thi TOEIC thực tế, ước gì ứng dụng có nhiều câu hỏi để tôi tha hồ làm hơn!",""),
            Evaluate("Hồ Thị Hương", "13:14 - 13/10/2023", 5, "Nguồn từ vựng và ngữ pháp của ứng dụng khá phong phú, giúp tôi nắm vững được kiến thức trước khi làm bài kiểm tra.",""),
            Evaluate("Trần Đức Tính", "08:34 - 18/8/2023", 4, "Ứng dụng đơn giản, dễ sử dụng, kiến thức vừa đủ.","")
        ))

        initViews()
    }

    private fun initViews() {
        binding.benefits.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = benefitAdapter
        }

        pageSnapHelper.attachToRecyclerView(binding.benefits)
        binding.circleIndicator.attachToRecyclerView(binding.benefits, pageSnapHelper)
        benefitAdapter.registerAdapterDataObserver(binding.circleIndicator.adapterDataObserver)

        handler.postDelayed(this, 3000)

        binding.userEvaluates.apply {
            adapter = InfiniteScrollAdapter.wrap(evaluateAdapter)
            setOrientation(DSVOrientation.HORIZONTAL)
            setHasFixedSize(true)
            setItemTransformer(
                ScaleTransformer.Builder()
                    .setMaxScale(1f)
                    .setMinScale(0.8f)
                    .setPivotX(Pivot.X.CENTER)
                    .setPivotY(Pivot.Y.BOTTOM)
                    .build()
            )
        }
    }

    private fun handleChangeUpgradeBenefits() {
        val currentAdsPosition = getCurrentItemAdsPosition()
        val newAdsPosition = (currentAdsPosition + 1) % benefitAdapter.itemCount

        val currentEvaluatePosition = binding.userEvaluates.currentItem

        binding.benefits.smoothScrollToPosition(newAdsPosition)
        binding.circleIndicator.animatePageSelected(newAdsPosition)
        binding.userEvaluates.smoothScrollToPosition(currentEvaluatePosition+1)
    }

    private fun getCurrentItemAdsPosition(): Int {
        val layoutManager = binding.benefits.layoutManager as LinearLayoutManager
        val snapView = pageSnapHelper.findSnapView(layoutManager)
        return snapView?.let { layoutManager.getPosition(it) } ?: 0
    }

    override fun run() {
        handleChangeUpgradeBenefits()
        handler.postDelayed(this, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(this)
    }
}