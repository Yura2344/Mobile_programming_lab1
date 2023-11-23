package com.example.lab1clock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import java.util.TimeZone

class ClockFragment : Fragment(R.layout.fragment_clock) {
    private lateinit var timezoneTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timezoneTextView = requireView().findViewById(R.id.timezoneTextView)


        updateCurrentTimezone()
    }

    fun updateCurrentTimezone() {
        val currentTimeZone: TimeZone = TimeZone.getDefault()
        timezoneTextView.text = getString(
            R.string.timezone_display,
            currentTimeZone.getDisplayName(false, TimeZone.SHORT), currentTimeZone.id
        )
    }
}