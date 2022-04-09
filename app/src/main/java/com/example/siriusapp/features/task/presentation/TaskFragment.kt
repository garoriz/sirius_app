package com.example.siriusapp.features.task.presentation

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.provider.Contacts
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.siriusapp.R
import com.example.siriusapp.databinding.FragmentTaskBinding
import com.example.siriusapp.features.task.presentation.service.SoundService
import kotlinx.coroutines.*


class TaskFragment : Fragment(R.layout.fragment_task) {
    private lateinit var binding: FragmentTaskBinding
    private var binder: SoundService.LocaleBinder? = null
    private var number1 = 0
    private var number2 = 0
    private var number3 = 0

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            binder = service as? SoundService.LocaleBinder
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            binder = null
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaskBinding.bind(view)

        activity?.bindService(
            Intent(this.context, SoundService::class.java),
            connection,
            BIND_AUTO_CREATE
        )

        with(binding) {
            btnStart.setOnClickListener {
                start()
            }
            btnCheck.setOnClickListener {
                check()
            }
            btnContinue.setOnClickListener {
                endTask()
            }
        }
    }

    private fun start() {
        with(binding) {
            lifecycleScope.launch {
                btnStart.visibility = View.INVISIBLE
                tvInfo.visibility = View.VISIBLE
                delay(1000L)
                tvInfo.text = getString(R.string._2)
                delay(1000L)
                tvInfo.text = getString(R.string._1)
                delay(1000L)
                tvInfo.text = getString(R.string.listen_sound)
                chooseAudios()
                binder?.play(R.raw.n20)
                btnCheck.visibility = View.VISIBLE
            }
        }
    }

    private fun chooseAudios() {
        number1 = 20
        number2 = 20
        number3 = 20
        /*while (number2 == number1) {
        number2 = (10..20).random()
    }
    while (number2 == number3 || number1 == number3) {
        number3 = (10..20).random()
    }*/
    }

    private fun startMedia(n1: Int, n2: Int, n3: Int) {
        //TODO
    }

    private fun check() {
        with(binding) {
            btnCheck.visibility = View.INVISIBLE
            btnContinue.visibility = View.VISIBLE
            edAnswer1.isEnabled = false
            edAnswer2.isEnabled = false
            edAnswer3.isEnabled = false
            if (edAnswer1.text.toString() == number1.toString()) {
                edAnswer1.setBackgroundResource(R.drawable.right_background)
            } else {
                edAnswer1.setText(number1.toString())
                edAnswer1.setBackgroundResource(R.drawable.error_background)
            }
            if (edAnswer2.text.toString() == number2.toString()) {
                edAnswer2.setBackgroundResource(R.drawable.right_background)
            } else {
                edAnswer2.setText(number2.toString())
                edAnswer2.setBackgroundResource(R.drawable.error_background)
            }
            if (edAnswer3.text.toString() == number3.toString()) {
                edAnswer3.setBackgroundResource(R.drawable.right_background)
            } else {
                edAnswer3.setText(number3.toString())
                edAnswer3.setBackgroundResource(R.drawable.error_background)
            }
        }
    }

    private fun endTask() {
        view?.findNavController()?.navigate(R.id.action_taskFragment_to_selectTaskFragment)
    }
}
