package com.yhezra.ayokelas.ui.navigationdrawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yhezra.ayokelas.api.RetrofitClientCovid
import com.yhezra.ayokelas.databinding.FragmentInfoCovid1Binding
import com.yhezra.ayokelas.model.IndonesiaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoCovidFragment : Fragment() {
    private lateinit var fragmentInfoCovidBinding: FragmentInfoCovid1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentInfoCovidBinding = FragmentInfoCovid1Binding.inflate(layoutInflater, container, false)
        return fragmentInfoCovidBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ShowIndonesia()
    }

    private fun ShowIndonesia() {
        RetrofitClientCovid.instance.getIndonesia().enqueue(object : Callback<ArrayList<IndonesiaResponse>>{
            override fun onFailure(call: Call<ArrayList<IndonesiaResponse>>, t: Throwable) {
                Toast.makeText(activity,"${t.message}",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ArrayList<IndonesiaResponse>>,
                response: Response<ArrayList<IndonesiaResponse>>
            ) {
                val indonesia = response.body()?.get(0)
                val positive = indonesia?.positif
                val hospitalized = indonesia?.dirawat
                val recover = indonesia?.sembuh
                val death = indonesia?.meninggal

                fragmentInfoCovidBinding.tvPositive.text = positive
                fragmentInfoCovidBinding.tvHospitalized.text = hospitalized
                fragmentInfoCovidBinding.tvRecover.text = recover
                fragmentInfoCovidBinding.tvDeath.text = death
            }
        })
    }

}