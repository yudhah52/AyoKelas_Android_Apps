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

class EnrolledAdapter(private val activity: Activity) :
    RecyclerView.Adapter<EnrolledAdapter.EnrolledViewHolder>() {
    private lateinit var auth: FirebaseAuth

    var listClassroom = ArrayList<Classroom>()
        set(listClassroom) {
            if (listClassroom.size > 0) {
                this.listClassroom.clear()
            }
            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            println(listClassroom)
            /*Item kelas yang dimasukkan ke dalam data untuk recyclerview hanya yang memiliki member
            dengan email yang sama dengan current user(user yang sedang aktif)*/
            for (classroom in listClassroom) {
                val members = classroom.members?.split(";")
                for (member in members!!) {
                    if (member != "") {
                        if (member.split("|")[1] == user?.email) {
                            this.listClassroom.add(classroom)
                        }

                    }
                }
            }

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrolledViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_classroom, parent, false)
        return EnrolledViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnrolledViewHolder, position: Int) {
        holder.bind(listClassroom[position])
    }

    override fun getItemCount(): Int = this.listClassroom.size

    inner class EnrolledViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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