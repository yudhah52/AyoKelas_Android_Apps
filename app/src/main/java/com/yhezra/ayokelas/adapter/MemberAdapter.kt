package com.yhezra.ayokelas.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.yhezra.ayokelas.R
import com.yhezra.ayokelas.databinding.ItemMemberBinding

class MemberAdapter(private val listMember: List<String>?, private val activity: Activity) :
    RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(listMember!![position])
    }

    override fun getItemCount(): Int = this.listMember!!.size

    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMemberBinding.bind(itemView)
        fun bind(listMember: String) {
            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            if (listMember != "") {
                binding.tvMemberName.text = listMember.split("|")[0]
            }
        }
    }
}