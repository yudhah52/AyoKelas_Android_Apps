package com.yhezra.ayokelas.ui.detailclassroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.yhezra.ayokelas.adapter.MemberAdapter
import com.yhezra.ayokelas.databinding.FragmentMemberBinding
import com.yhezra.ayokelas.db.ClassroomHelper
import com.yhezra.ayokelas.entity.Classroom

class MemberFragment : Fragment() {

    private var classroom: Classroom? = null
    private lateinit var adapter: MemberAdapter
    private lateinit var binding: FragmentMemberBinding
    private lateinit var classroomHelper: ClassroomHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemberBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classroomHelper = ClassroomHelper.getInstance(requireContext())
        classroomHelper.open()

        classroom = activity?.intent?.getParcelableExtra(DetailClassroomActivity.EXTRA_CLASSROOM)

        var listMember = classroom?.members?.split(";")

        binding.rvMember.layoutManager = LinearLayoutManager(activity)
        binding.rvMember.setHasFixedSize(true)
        adapter = MemberAdapter(listMember,requireActivity())
        binding.rvMember.adapter = adapter
    }

}