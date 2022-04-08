package com.example.siriusapp.features.home.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.siriusapp.R
import com.example.siriusapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        with(binding) {
            btnTraining.setOnClickListener {
                view.findNavController().navigate(R.id.action_navigation_home_to_selectTaskFragment)
            }
        }
    }
}
