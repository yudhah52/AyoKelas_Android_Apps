package com.yhezra.ayokelas.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.yhezra.ayokelas.clicklistener.CustomOnItemClickListener
import com.yhezra.ayokelas.ui.detailclassroom.DetailClassroomActivity
import com.yhezra.ayokelas.R
import com.yhezra.ayokelas.databinding.ItemClassroomBinding
import com.yhezra.ayokelas.entity.Classroom

class AllClassroomsAdapter(private val activity: Activity) :
    RecyclerView.Adapter<AllClassroomsAdapter.ClassroomViewHolder>() {
    private lateinit var auth: FirebaseAuth

    var listClassroom = ArrayList<Classroom>()
        set(listClassroom) {
            if (listClassroom.size > 0) {
                this.listClassroom.clear()
            }

            this.listClassroom.addAll(listClassroom)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_classroom, parent, false)
        return ClassroomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassroomViewHolder, position: Int) {
        holder.bind(listClassroom[position])
    }

    override fun getItemCount(): Int = this.listClassroom.size

    inner class ClassroomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemClassroomBinding.bind(itemView)
        fun bind(classroom: Classroom) {
            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            binding.tvClassName.text = classroom.title
            binding.tvDate.text = classroom.date
            binding.tvSchedule.text = classroom.schedule
            binding.tvUserEmail.text = classroom.userEmail
            binding.tvTeacher.text = classroom.userName

            binding.cvItemClassroom.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(activity, DetailClassroomActivity::class.java)
                            intent.putExtra(DetailClassroomActivity.EXTRA_POSITION, position)
                            intent.putExtra(DetailClassroomActivity.EXTRA_CLASSROOM, classroom)
                            activity.startActivityForResult(
                                intent,
                                DetailClassroomActivity.REQUEST_UPDATE
                            )
                        }
                    })
            )
        }
    }
}