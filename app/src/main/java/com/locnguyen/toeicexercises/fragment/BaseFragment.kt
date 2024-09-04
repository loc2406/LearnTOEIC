package com.locnguyen.toeicexercises.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class BaseFragment(inheritClassName: String): Fragment() {
    private val CLASS_NAME: String by lazy {inheritClassName}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(CLASS_NAME, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(CLASS_NAME, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(CLASS_NAME, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(CLASS_NAME, "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d(CLASS_NAME, "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(CLASS_NAME, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(CLASS_NAME, "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d(CLASS_NAME, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(CLASS_NAME, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(CLASS_NAME, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(CLASS_NAME, "onDetach")
    }
}