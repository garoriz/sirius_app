package com.example.siriusapp.features.selectTask.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.siriusapp.R
import com.example.siriusapp.databinding.FragmentHomeBinding
import com.example.siriusapp.databinding.FragmentSelectTaskBinding

class SelectTaskFragment : Fragment(R.layout.fragment_select_task) {

    private lateinit var binding: FragmentSelectTaskBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSelectTaskBinding.bind(view)

        with(binding) {
            btnNums.setOnClickListener {
                view.findNavController().navigate(R.id.action_selectTaskFragment_to_selectNumsFragment2)
            }
        }
    }

}
