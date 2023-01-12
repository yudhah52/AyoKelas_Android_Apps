package com.yhezra.ayokelas.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yhezra.ayokelas.R
import com.yhezra.ayokelas.databinding.FragmentEnrolledBinding
import com.yhezra.ayokelas.databinding.FragmentProfileBinding

class EnrolledFragment : Fragment() {

    private lateinit var fragmentEnrolledBinding : FragmentEnrolledBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentEnrolledBinding = FragmentEnrolledBinding.inflate(layoutInflater, container, false)
        return fragmentEnrolledBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}