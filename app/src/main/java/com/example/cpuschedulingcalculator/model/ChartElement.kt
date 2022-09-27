package com.example.cpuschedulingcalculator.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChartElement(
    val processName: String,

    var completionTime: Int
) : Parcelable
