package com.yhezra.ayokelas.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.yhezra.ayokelas.ClassroomAddUpdateActivity
import com.yhezra.ayokelas.CustomOnItemClickListener
import com.yhezra.ayokelas.R
import com.yhezra.ayokelas.databinding.ItemClassroomBinding
import com.yhezra.ayokelas.entity.Classroom

class AllClassroomsAdapter(private val activity: Activity): RecyclerView.Adapter<AllClassroomsAdapter.ClassroomViewHolder>() {
    private lateinit var auth: FirebaseAuth

    var listClassroom = ArrayList<Classroom>()
        set(listClassroom) {
            if (listClassroom.size > 0) {
                this.listClassroom.clear()
            }
            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            this.listClassroom.addAll(listClassroom)

            notifyDataSetChanged()
        }

//    fun addItem(classroom: Classroom) {
//        this.listClassroom.add(classroom)
//        notifyItemInserted(this.listClassroom.size - 1)
//    }
//
//    fun updateItem(position: Int, classroom: Classroom) {
//        this.listClassroom[position] = classroom
//        notifyItemChanged(position, classroom)
//    }
//
//    fun removeItem(position: Int) {
//        this.listClassroom.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, this.listClassroom.size)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_classroom, parent, false)
        return ClassroomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassroomViewHolder, position: Int) {
        holder.bind(listClassroom[position])
    }

    override fun getItemCount(): Int = this.listClassroom.size

    inner class ClassroomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemClassroomBinding.bind(itemView)
        fun bind(classroom: Classroom) {
            binding.tvClassName.text = classroom.title
            binding.tvDate.text = classroom.date
            binding.tvDesc.text = classroom.description
            binding.tvTeacher.text = classroom.teacher
            binding.tvUser.text = classroom.user
//            binding.cvItemClassroom.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
//                override fun onItemClicked(view: View, position: Int) {
//                    val intent = Intent(activity, ClassroomAddUpdateActivity::class.java)
//                    intent.putExtra(ClassroomAddUpdateActivity.EXTRA_POSITION, position)
//                    intent.putExtra(ClassroomAddUpdateActivity.EXTRA_CLASSROOM, classroom)
//                    activity.startActivityForResult(intent, ClassroomAddUpdateActivity.REQUEST_UPDATE)
//                }
//            }))
        }
    }
}