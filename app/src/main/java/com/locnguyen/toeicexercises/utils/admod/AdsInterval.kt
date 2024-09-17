package com.locnguyen.toeicexercises.utils.admod

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Bundle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.locnguyen.toeicexercises.utils.GlobalHelper

class AdsInterval(private val context: Context): AdListener() {
    private var myInterstitialAd: InterstitialAd? = null
    private var interstitialAdTime = 3600f // Thời gian nghỉ giữa 2 lần quảng cáo, tối thiểu là 1 phút
    private var onCloseListener: (() -> Unit)? = null
    private var dataBundle: Bundle? = null
    private var idAds: String? = null



    init{
        initDataInterval()
        createFullAds()
    }

    private fun initDataInterval() {
        val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

        idAds = if (isDebuggable){
            GlobalHelper(context).idAdsIntervalTest
        }else{
            GlobalHelper(context).idAdsInterval
        }
    }

    fun createFullAds(){
        if (myInterstitialAd != null){
             return
        }

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, idAds!!, adRequest, object: InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                myInterstitialAd = null
            }

            override fun onAdLoaded(p0: InterstitialAd) {
                myInterstitialAd = p0
                myInterstitialAd!!.fullScreenContentCallback = object: FullScreenContentCallback(){
                    override fun onAdClicked() {
                        super.onAdClicked()
                    }

                    override fun onAdShowedFullScreenContent() {
                        myInterstitialAd = null
                        createFullAds()
                    }

                    override fun onAdDismissedFullScreenContent() {
                       dataBundle = null
                        createFullAds()
                    }
                }
            }
        })
    }

    fun showIntervalAds(){
        if (myInterstitialAd == null) return
        if (adShowAble()){
            myInterstitialAd!!.show(context as Activity)
        }
    }

    private fun adShowAble(): Boolean {
        return true
    }

    override fun onAdClosed() {
        super.onAdClosed()
        onCloseListener?.invoke()
    }

    override fun onAdClicked() {
        super.onAdClicked()

    }
}