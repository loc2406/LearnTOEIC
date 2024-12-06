package com.locnguyen.toeicexercises.fragment

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.SettingFragmentBinding
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.utils.loadImg
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.main.MainVM
import com.locnguyen.toeicexercises.viewmodel.main.setting.SettingVM
import gun0912.tedimagepicker.builder.TedImagePicker

class SettingFragment: Fragment() {

    private lateinit var binding: SettingFragmentBinding
    private val settingVM: SettingVM by viewModels<SettingVM>()
    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val loadingDialog: Dialog by lazy {DialogHelper.getLoadingDialog(requireContext())}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        binding.settingVM = settingVM
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserves()
    }

    private fun initObserves() {
        settingVM.changeImgClicked.observe(viewLifecycleOwner){ isClicked ->
            isClicked.getContentIfNotHandled()?.let {
                handleGetImgFromGallery()
            }
        }

        settingVM.logoutClicked.observe(viewLifecycleOwner){ isClicked ->
            isClicked.getContentIfNotHandled()?.let {
                mainVM.logoutClicked()
            }
        }

        settingVM.imgUrl.observe(viewLifecycleOwner){ url ->
            url?.let {
                requireContext().loadImg(img = it, view = binding.img, listener = object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        requireContext().toastMessage(R.string.Load_user_img_failed)
                        if (loadingDialog.isShowing){
                            loadingDialog.dismiss()
                        }
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        requireContext().toastMessage(R.string.Load_user_img_successful)
                        if (loadingDialog.isShowing){
                            loadingDialog.dismiss()
                        }
                        return false
                    }
                })
            }
        }
    }

    private fun handleGetImgFromGallery() {
        TedImagePicker.with(requireContext())
            .start { uri ->
                loadingDialog.show()
                handleUpdateNewImg(uri)
            }
    }

    private fun handleUpdateNewImg(newImg: Uri) {
        settingVM.updateNewImg(newImg)
    }
}