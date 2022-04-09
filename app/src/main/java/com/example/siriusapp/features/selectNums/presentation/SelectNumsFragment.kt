package com.example.siriusapp.features.selectNums.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.siriusapp.R
import com.example.siriusapp.databinding.FragmentSelectNumsBinding
import com.example.siriusapp.databinding.FragmentSelectTaskBinding

class SelectNumsFragment : Fragment(R.layout.fragment_select_nums) {

    private lateinit var binding: FragmentSelectNumsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSelectNumsBinding.bind(view)

        with(binding) {
            btnTwoDigit.setOnClickListener {
                view.findNavController().navigate(R.id.action_selectNumsFragment2_to_taskFragment)
            }
        }
    }
}
