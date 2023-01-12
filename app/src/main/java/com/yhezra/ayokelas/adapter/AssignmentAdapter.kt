package com.yhezra.ayokelas.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yhezra.ayokelas.R
import com.yhezra.ayokelas.databinding.ItemAssignmentBinding

class AssignmentAdapter(private val listAssignment: List<String>?, private val activity: Activity) :
    RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_assignment, parent, false)
        return AssignmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        holder.bind(listAssignment!![position])
    }

    override fun getItemCount(): Int = this.listAssignment!!.size

    inner class AssignmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemAssignmentBinding.bind(itemView)
        fun bind(listAssignment: String) {

            if (listAssignment != "") {
                binding.tvTask.text = listAssignment.split("|")[0]
                binding.tvDueDate.text = listAssignment.split("|")[1]
            }
        }
    }
}