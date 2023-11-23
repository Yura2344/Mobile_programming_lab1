package com.example.lab1clock

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import java.util.TimeZone

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var timezonesSpinner: Spinner
    private lateinit var setTimezoneButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timezonesSpinner = requireView().findViewById(R.id.timezonesSpinner)
        setTimezoneButton = requireView().findViewById(R.id.setTimezoneButton)

        val timezones = TimeZone.getAvailableIDs()
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_spinner_item, timezones
        )
        timezonesSpinner.adapter = adapter

        setTimezoneButton.setOnClickListener {
            TimeZone.setDefault(TimeZone.getTimeZone(timezonesSpinner.selectedItem.toString()))
            (requireActivity().supportFragmentManager.findFragmentByTag("f0") as ClockFragment).updateCurrentTimezone()
        }
    }

}