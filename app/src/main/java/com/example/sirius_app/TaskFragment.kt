package com.example.sirius_app

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sirius_app.databinding.FragmentTaskBinding

class TaskFragment : Fragment(R.layout.fragment_task) {
    private lateinit var binding: FragmentTaskBinding
    private var number1 = 0
    private var number2 = 0
    private var number3 = 0
    private lateinit var media1: MediaPlayer
    private lateinit var media2: MediaPlayer
    private lateinit var media3: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chooseAudios()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaskBinding.bind(view)

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
            btnStart.visibility = View.INVISIBLE
            tvInfo.visibility = View.VISIBLE
            Thread.sleep(1000)
            tvInfo.text = "2..."
            Thread.sleep(1000)
            tvInfo.text = "1..."
            Thread.sleep(1000)
            tvInfo.text = "Прослушайте запись..."
            chooseAudios()
            startMedia()
            btnCheck.visibility = View.VISIBLE
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
        media1 = MediaPlayer.create(
            activity,
            Uri.parse("android.resource://$PACKAGE_NAME/raw/n$number1")
        )
        media2 = MediaPlayer.create(
            activity,
            Uri.parse("android.resource://$PACKAGE_NAME/raw/n$number2")
        )
        media3 = MediaPlayer.create(
            activity,
            Uri.parse("android.resource://$PACKAGE_NAME/raw/n$number3")
        )
        media1.start()
        Thread.sleep(1000)
        media2.start()
        Thread.sleep(1000)
        media3.start()
        Thread.sleep(1000)
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
                edAnswer1.setText(number1)
                edAnswer1.setBackgroundResource(R.drawable.error_background)
                //TODO add to db
            }
            if (edAnswer2.text.toString() == number2.toString()) {
                edAnswer2.setBackgroundResource(R.drawable.right_background)
            } else {
                edAnswer2.setText(number2)
                edAnswer2.setBackgroundResource(R.drawable.error_background)
                //TODO add to db
            }
            if (edAnswer3.text.toString() == number3.toString()) {
                edAnswer3.setBackgroundResource(R.drawable.right_background)
            } else {
                edAnswer3.setText(number3)
                edAnswer3.setBackgroundResource(R.drawable.error_background)
                //TODO add to db
            }
        }
    }

    private fun endTask() {
        findNavController().navigate(R.id.action_taskFragment_to_selectTaskFragment)
    }
}