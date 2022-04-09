package com.example.siriusapp.features.task.presentation

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.siriusapp.R
import com.example.siriusapp.databinding.FragmentTaskBinding
import com.example.siriusapp.features.task.presentation.service.SoundService
import kotlinx.coroutines.*
import java.util.ArrayList


class TaskFragment : Fragment(R.layout.fragment_task) {
    private lateinit var binding: FragmentTaskBinding
    private var binder: SoundService.LocaleBinder? = null
    private var number1 = 0
    private var number2 = 0
    private var number3 = 0
    private var arrayEt = ArrayList<EditText>(3)
    private var current: Int = 0
    private var isCheck: Boolean = false

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
            changeEditTexts()
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
                startMedia()
                btnCheck.visibility = View.VISIBLE
            }
        }
    }

    private fun chooseAudios() {
        number1 = (10..20).random()
        number2 = (10..20).random()
        number3 = (10..20).random()
        while (number2 == number1) {
            number2 = (10..20).random()
        }
        while (number2 == number3 || number1 == number3) {
            number3 = (10..20).random()
        }
    }

    private fun startMedia() {
        lifecycleScope.launch {
            binder?.play(resources.getIdentifier("n" + 20.toString(), "raw", activity?.packageName))
            delay(400)
            binder?.play(R.raw.n17eng)
            delay(400)
            binder?.play(R.raw.n14)
        }
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

    private fun changeEditTexts() {
        with(binding) {
            arrayEt.add(edAnswer1)
            arrayEt.add(edAnswer2)
            arrayEt.add(edAnswer3)
            edAnswer1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 2 && !isCheck) {
                        current++
                        if (current < 4) {
                            arrayEt[current].requestFocus()
                        }
                    }
                }
            })
            edAnswer2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 2 && !isCheck) {
                        current++
                        if (current < 4) {
                            arrayEt[current].requestFocus()
                        }
                    }
                }
            })
            edAnswer3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 2 && !isCheck) {
                        ViewCompat.getWindowInsetsController(requireView())
                            ?.hide(WindowInsetsCompat.Type.ime())
                    }
                }
            })
        }
    }
}
