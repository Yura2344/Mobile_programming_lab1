package com.example.lab1clock

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class TimerFragment : Fragment(R.layout.fragment_timer) {
    companion object {
        const val DEFAULT_TIME_STRING: String = "00:00.00"
    }

    private var milliseconds: Long = 0
    private var startTime: Long = 0
    private var lastRound: Long = 0

    private var isTimerActive: Boolean = false
    private var isTimerPaused: Boolean = false

    private var handler: Handler = Handler(Looper.getMainLooper())
    private var roundsList: ArrayList<String> = arrayListOf<String>()

    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var timeTextBox: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var pauseButton: Button
    private lateinit var roundButton: Button
    private lateinit var roundsListView: ListView

    private var timeCounter: Runnable = object : Runnable {
        override fun run() {
            milliseconds = System.currentTimeMillis() - startTime

            timeTextBox.text = getTimeString(milliseconds)

            if (isTimerActive && !isTimerPaused)
                handler.post(this)
        }
    }

    fun getTimeString(milliseconds: Long): String {
        return String.format(
            "%02d:%02d.%02d",
            milliseconds / 60000,
            milliseconds / 1000,
            (milliseconds % 1000) / 10
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            roundsList = savedInstanceState.getStringArrayList("roundsList")!!

            milliseconds = savedInstanceState.getLong("milliseconds")
            startTime = savedInstanceState.getLong("startTime")
            lastRound = savedInstanceState.getLong("lastRound")

            isTimerActive = savedInstanceState.getBoolean("isTimerActive")
            isTimerPaused = savedInstanceState.getBoolean("isTimerPaused")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArrayList("roundsList", roundsList)

        outState.putLong("milliseconds", milliseconds)
        outState.putLong("startTime", startTime)
        outState.putLong("lastRound", lastRound)

        outState.putBoolean("isTimerActive", isTimerActive)
        outState.putBoolean("isTimerPaused", isTimerPaused)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeTextBox = requireView().findViewById(R.id.measuredTimeTextView)
        startButton = requireView().findViewById(R.id.startButton)
        stopButton = requireView().findViewById(R.id.stopButton)
        pauseButton = requireView().findViewById(R.id.pauseButton)
        roundButton = requireView().findViewById(R.id.roundButton)
        roundsListView = requireView().findViewById(R.id.roundsListView)
        timeTextBox.text = DEFAULT_TIME_STRING

        adapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1, roundsList
        )
        roundsListView.adapter = adapter

        startButton.setOnClickListener { startTimer() }
        stopButton.setOnClickListener { stopTimer() }
        pauseButton.setOnClickListener { if (isTimerActive) pauseTimer() }
        roundButton.setOnClickListener { if (isTimerActive) addRound() }

        if (isTimerPaused) {
            pauseButton.text = getString(R.string.continue_button_text)
            timeTextBox.text = getTimeString(milliseconds)
        }

        if (isTimerActive && !isTimerPaused)
            handler.post(timeCounter)
    }

    private fun startTimer() {
        isTimerActive = true
        isTimerPaused = false
        lastRound = 0
        pauseButton.text = getString(R.string.pause_button_text)
        startTime = System.currentTimeMillis()
        roundsList.clear()
        handler.post(timeCounter)
        adapter.notifyDataSetChanged()
    }

    private fun stopTimer() {
        isTimerPaused = false
        isTimerActive = false
        pauseButton.text = getString(R.string.pause_button_text)
        handler.post { timeTextBox.text = DEFAULT_TIME_STRING }
        adapter.notifyDataSetChanged()
    }

    private fun pauseTimer() {
        isTimerPaused = !isTimerPaused

        if (!isTimerPaused) {
            pauseButton.text = getString(R.string.pause_button_text)
            startTime += System.currentTimeMillis() - (startTime + milliseconds)
            handler.post(timeCounter)
        } else {
            pauseButton.text = getString(R.string.continue_button_text)
        }
    }

    private fun addRound() {
        val timeDifference: Long = milliseconds - lastRound
        roundsList.add("+${getTimeString(timeDifference)}")
        lastRound = milliseconds
        adapter.notifyDataSetChanged()
    }
}