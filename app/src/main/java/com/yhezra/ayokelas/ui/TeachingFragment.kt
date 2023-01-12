package com.yhezra.ayokelas.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.yhezra.ayokelas.ClassroomAddUpdateActivity
import com.yhezra.ayokelas.adapter.TeachingAdapter
import com.yhezra.ayokelas.databinding.FragmentTeachingBinding
import com.yhezra.ayokelas.db.ClassroomHelper
import com.yhezra.ayokelas.entity.Classroom
import com.yhezra.ayokelas.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TeachingFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var adapter: TeachingAdapter
    private lateinit var binding: FragmentTeachingBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeachingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvClassroom.layoutManager = LinearLayoutManager(activity)
        binding.rvClassroom.setHasFixedSize(true)
        adapter = TeachingAdapter(requireActivity())
        binding.rvClassroom.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(activity, ClassroomAddUpdateActivity::class.java)
            startActivityForResult(intent, ClassroomAddUpdateActivity.REQUEST_ADD)
        }

        if (savedInstanceState == null) {
            loadClassroomAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Classroom>(EXTRA_STATE)
            if (list != null) {
                adapter.listClassroom = list
            }
        }
    }

    private fun loadClassroomAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressbar.visibility = View.VISIBLE
            val classroomHelper = ClassroomHelper.getInstance(requireContext())
            classroomHelper.open()
            val deferredClassroom = async(Dispatchers.IO) {
                val cursor = classroomHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            binding.progressbar.visibility = View.INVISIBLE
            val classroom = deferredClassroom.await()
            if (classroom.size > 0) {
                adapter.listClassroom = classroom
            } else {
                adapter.listClassroom = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
            classroomHelper.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listClassroom)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                ClassroomAddUpdateActivity.REQUEST_ADD -> if (resultCode == ClassroomAddUpdateActivity.RESULT_ADD) {
                    val classroom =
                        data.getParcelableExtra<Classroom>(ClassroomAddUpdateActivity.EXTRA_CLASSROOM) as Classroom

                    adapter.addItem(classroom)
                    binding.rvClassroom.smoothScrollToPosition(adapter.itemCount - 1)

                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                ClassroomAddUpdateActivity.REQUEST_UPDATE ->
                    when (resultCode) {
                        ClassroomAddUpdateActivity.RESULT_UPDATE -> {

                            val classroom =
                                data.getParcelableExtra<Classroom>(ClassroomAddUpdateActivity.EXTRA_CLASSROOM) as Classroom
                            val position =
                                data.getIntExtra(ClassroomAddUpdateActivity.EXTRA_POSITION, 0)

                            adapter.updateItem(position, classroom)
                            binding.rvClassroom.smoothScrollToPosition(position)

                            showSnackbarMessage("Satu item berhasil diubah")
                        }
                        ClassroomAddUpdateActivity.RESULT_DELETE -> {
                            val position =
                                data.getIntExtra(ClassroomAddUpdateActivity.EXTRA_POSITION, 0)

                            adapter.removeItem(position)

                            showSnackbarMessage("Satu item berhasil dihapus")
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadClassroomAsync()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvClassroom, message, Snackbar.LENGTH_SHORT).show()
    }


}