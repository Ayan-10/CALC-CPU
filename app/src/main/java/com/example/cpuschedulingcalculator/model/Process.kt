package com.example.cpuschedulingcalculator.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Process(

    val name: String,

    var arrivalTime: Int,

    var burstTime: Int,

    var priority: Int = 0,

    var firstResponse: Int = 0

) : Parcelable
