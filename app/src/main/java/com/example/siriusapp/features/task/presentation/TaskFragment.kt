package com.example.siriusapp.features.task.presentation

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.siriusapp.R
import com.example.siriusapp.databinding.FragmentTaskBinding
import com.example.siriusapp.features.task.data.db.ActsDbHelper
import com.example.siriusapp.features.task.presentation.service.SoundService
import com.example.siriusapp.model.Settings
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
    private lateinit var soundDb: SQLiteDatabase
    private val listSounds = mutableListOf<String>()
    private val listAnswers = mutableListOf<Int>()
    private var score: Int = 0
    private lateinit var cursor: Cursor
    private var arrayBtns = ArrayList<Button>(4)
    private var rightAnswer = 0

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

        chooseAudios()

        lifecycleScope.launch {
            soundDb = ActsDbHelper(requireContext()).readableDatabase

            if (Settings.accent == "uk") {
                cursor = soundDb.rawQuery("SELECT * FROM sounds_en", null)
            } else if (Settings.accent == "fr") {
                cursor = soundDb.rawQuery("SELECT * FROM sounds_fr", null)
            }


            randomSounds(number1)
            randomSounds(number2)
            randomSounds(number3)
        }

        activity?.bindService(
            Intent(this.context, SoundService::class.java),
            connection,
            BIND_AUTO_CREATE
        )

        if (!Settings.isHardDiff) {
            with(binding) {
                edAnswer1.visibility = View.INVISIBLE
                edAnswer2.visibility = View.INVISIBLE
                edAnswer3.visibility = View.INVISIBLE
                arrayBtns.add(btnAnswer1)
                arrayBtns.add(btnAnswer2)
                arrayBtns.add(btnAnswer3)
                arrayBtns.add(btnAnswer4)
                for (btn in arrayBtns) {
                    val t1 = (10..90).random()
                    val t2 = (10..90).random()
                    val t3 = (10..90).random()
                    btn.text = "$t1, $t2, $t3"
                }
                val t3 = (0..3).random()
                rightAnswer = t3
                arrayBtns[t3].text =
                    listAnswers[0].toString() + ", " + listAnswers[1].toString() + ", " + listAnswers[2].toString()
            }
        }

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
            btnAnswer1.setOnClickListener {
                checkSimple(arrayBtns[0], arrayBtns[rightAnswer])
            }
            btnAnswer2.setOnClickListener {
                checkSimple(arrayBtns[1], arrayBtns[rightAnswer])
            }
            btnAnswer3.setOnClickListener {
                checkSimple(arrayBtns[2], arrayBtns[rightAnswer])
            }
            btnAnswer4.setOnClickListener {
                checkSimple(arrayBtns[3], arrayBtns[rightAnswer])
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
                for (btn in arrayBtns) {
                    btn.visibility = View.VISIBLE
                }
                startMedia()
            }
        }
    }

    private fun chooseAudios() {
        number1 = (0..9).random()
        number2 = (0..9).random()
        number3 = (0..9).random()
        while (number2 == number1) {
            number2 = (0..9).random()
        }
        while (number2 == number3 || number1 == number3) {
            number3 = (0..9).random()
        }
    }

    private fun startMedia() {
        lifecycleScope.launch {

            startOneSound(listSounds[0])
            startOneSound(listSounds[1])
            startOneSound(listSounds[2])
            if (Settings.isHardDiff) {
                binding.btnCheck.visibility = View.VISIBLE
            }
        }
    }

    private fun randomSounds(number: Int) {
        cursor.moveToFirst()
        for (i in 1..number) {
            cursor.moveToNext()
        }
        listSounds.add(cursor.getString(1))
        listAnswers.add(cursor.getInt(2))
    }

    private suspend fun startOneSound(soundName: String) {
        binder?.play(resources.getIdentifier(soundName, "raw", activity?.packageName))
        delay(1500)
    }

    private fun check() {
        isCheck = true
        with(binding) {
            btnCheck.visibility = View.INVISIBLE
            btnContinue.visibility = View.VISIBLE
            edAnswer1.isEnabled = false
            edAnswer2.isEnabled = false
            edAnswer3.isEnabled = false
            if (edAnswer1.text.toString() == listAnswers[0].toString()) {
                edAnswer1.setBackgroundResource(R.drawable.right_background)
                score++
            } else {
                edAnswer1.setText(getString(R.string.right_answer) + listAnswers[0].toString())
                edAnswer1.setBackgroundResource(R.drawable.error_background)
            }
            if (edAnswer2.text.toString() == listAnswers[1].toString()) {
                edAnswer2.setBackgroundResource(R.drawable.right_background)
                score++
            } else {
                edAnswer2.setText(getString(R.string.right_answer) + listAnswers[1].toString())
                edAnswer2.setBackgroundResource(R.drawable.error_background)
            }
            if (edAnswer3.text.toString() == listAnswers[2].toString()) {
                edAnswer3.setBackgroundResource(R.drawable.right_background)
                score++
            } else {
                edAnswer3.setText(getString(R.string.right_answer) + listAnswers[2].toString())
                edAnswer3.setBackgroundResource(R.drawable.error_background)
            }
            when (score) {
                3 -> tvInfo.text = "Молодец! 3 из 3"
                2 -> tvInfo.text = "Всего одна ошибка! 2 из 3"
                1 -> tvInfo.text = "1 из 3"
                0 -> tvInfo.text = "0 из 3"
            }
            score = 0
        }
    }

    private fun checkSimple(selectedBtn: Button, rightBtn: Button) {
        binding.btnCheck.visibility = View.INVISIBLE
        if (selectedBtn.text.toString() == "listAnswers[0].toString() + \", \" + listAnswers[1].toString() + \", \" + listAnswers[2].toString()") {
            for (btn in arrayBtns)
                btn.setBackgroundResource(R.drawable.error_background)
            selectedBtn.setBackgroundResource(R.drawable.right_background)
        } else {
            for (btn in arrayBtns)
                btn.setBackgroundResource(R.drawable.error_background)
            rightBtn.setBackgroundResource(R.drawable.right_background)
        }
        binding.btnContinue.visibility = View.VISIBLE
    }

    private fun endTask() {
        view?.findNavController()?.navigateUp()
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
