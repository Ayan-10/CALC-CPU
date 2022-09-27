package com.example.cpuschedulingcalculator.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(

    var processName: String,

    var turnAroundTime : Int,

    var waitingTime : Int,

    var responseTime: Int,

    var relativeDelay: Double,

    var completionTime : Int

) : Parcelable
