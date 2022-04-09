package com.example.siriusapp.features.settings.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import com.example.siriusapp.MainActivity
import com.example.siriusapp.R
import com.example.siriusapp.databinding.FragmentSettingsBinding
import com.example.siriusapp.model.Settings

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        setColorsToButtons()
        setSwitches()

        with(binding) {
            switchFr.setOnClickListener {
                if (switchFr.isChecked) {
                    switchUk.isChecked = false
                    Settings.accent = "fr"
                    activity?.let {
                        (it as MainActivity).saveSettings()
                    }
                }
            }
            switchUk.setOnClickListener {
                if (switchUk.isChecked) {
                    switchFr.isChecked = false
                    Settings.accent = "uk"
                    activity?.let {
                        (it as MainActivity).saveSettings()
                    }
                }
            }
            btnEasy.setOnClickListener {
                Settings.isHardDiff = false
                setEasyIsLeader()
                activity?.let {
                    (it as MainActivity).saveSettings()
                }
            }
            btnHard.setOnClickListener {
                Settings.isHardDiff = true
                setHardIsLeader()
                activity?.let {
                    (it as MainActivity).saveSettings()
                }
            }
        }
    }

    private fun setColorsToButtons() {
        if (Settings.isHardDiff) {
            setHardIsLeader()
        } else {
            setEasyIsLeader()
        }
    }

    private fun setSwitches(){
        with(binding){
            when(Settings.accent){
                "uk" -> switchUk.isChecked = true
                "fr" -> switchFr.isChecked = true
            }
        }
    }

    private fun setHardIsLeader() {
        with(binding){
            btnEasy.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.custom))
            btnEasy.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            btnHard.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
            btnEasy.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun setEasyIsLeader() {
        with(binding){
            btnEasy.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
            btnEasy.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            btnHard.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.custom))
            btnEasy.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }
}
