package com.yhezra.ayokelas.ui.detailclassroom

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.yhezra.ayokelas.adapter.AssignmentAdapter
import com.yhezra.ayokelas.databinding.FragmentAssignmentBinding
import com.yhezra.ayokelas.db.ClassroomHelper
import com.yhezra.ayokelas.entity.Classroom

class AssignmentFragment : Fragment() {

    private var isTeacher = false
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: FragmentAssignmentBinding
    private var classroom: Classroom? = null
    private var position: Int = 0
    private lateinit var adapter : AssignmentAdapter
    private lateinit var classroomHelper: ClassroomHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAssignmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classroomHelper = ClassroomHelper.getInstance(requireContext())
        classroomHelper.open()

        classroom = activity?.intent?.getParcelableExtra(DetailClassroomActivity.EXTRA_CLASSROOM)
        position = activity?.intent?.getIntExtra(DetailClassroomActivity.EXTRA_POSITION, 0)!!

        classroom?.let {
            binding.tvClassName.setText(it.title)
            binding.tvDesc.setText(it.description)
            binding.tvTeacher.setText(": ${it.userName}")
            binding.tvSchedule.setText(": ${it.schedule}")
        }

        binding.fabAdd.setOnClickListener{
            val intent = Intent(activity, AssignmentAddUpdateActivity::class.java)
            intent.putExtra(AssignmentAddUpdateActivity.EXTRA_POSITION, position)
            intent.putExtra(AssignmentAddUpdateActivity.EXTRA_CLASSROOM, classroom)
            startActivityForResult(intent, AssignmentAddUpdateActivity.REQUEST_ADD)
            activity?.finish()
        }

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (classroom?.userEmail == user?.email) {
            isTeacher = true
        }
        if(isTeacher){
            binding.fabAdd.visibility = View.VISIBLE
        }

        var listAssignment = classroom?.assignments?.split(";")

        binding.rvAssignment.layoutManager = LinearLayoutManager(activity)
        binding.rvAssignment.setHasFixedSize(true)
        adapter = AssignmentAdapter(listAssignment,requireActivity())
        binding.rvAssignment.adapter = adapter


    }
}