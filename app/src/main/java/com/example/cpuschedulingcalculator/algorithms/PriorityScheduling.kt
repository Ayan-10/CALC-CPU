package com.example.cpuschedulingcalculator.algorithms

import com.example.cpuschedulingcalculator.model.ChartElement
import com.example.cpuschedulingcalculator.model.Process
import com.example.cpuschedulingcalculator.model.Result

class PriorityScheduling {
    private lateinit var results : Array<Result?>
    private var queueItems: ArrayList<ChartElement> = ArrayList()

    fun getNonPreemptiveResult(processes: Array<Process?>): Array<Result?> {
        results = arrayOfNulls(processes.size)
        processes.sortBy { process -> process!!.priority }

        // calculating waiting time

        // calculating waiting time
        for (i in processes.indices) {
            var completionTime = 0
            if( i == 0)
            {
                completionTime = processes[i]!!.arrivalTime + processes[i]!!.burstTime
            }
            else
            {
                if(  processes[i]!!.arrivalTime > results[i - 1]!!.completionTime)
                {
                    completionTime = processes[i]!!.arrivalTime + processes[i]!!.burstTime
                }
                else {
                    completionTime = results[i - 1]!!.completionTime + processes[i]!!.burstTime
                }
            }
            var turnAroundTime = completionTime - processes[i]!!.arrivalTime
            var waitingTime = if (i==0) processes[i]!!.arrivalTime else results[i - 1]!!.completionTime
            var responseTime = if (i==0) processes[i]!!.arrivalTime else results[i - 1]!!.completionTime
            var relativeDelay = if(processes[i]!!.arrivalTime == 0) 0.0 else (turnAroundTime.toDouble() / processes[i]!!.arrivalTime)
            val result = Result(processes[i]!!.name, turnAroundTime,waitingTime,responseTime,relativeDelay,completionTime)
            val chartElement = ChartElement(processes[i]!!.name, completionTime)
            results[i] = result
            queueItems.add(chartElement)
        }
        return results
    }


    fun getPreemptiveResult(processes: Array<Process?>): Array<Result?> {
        results = arrayOfNulls(processes.size)
        processes.sortBy { process -> process!!.priority }
        val burst_remaining: Array<Int?> =  arrayOfNulls(processes.size)
        var is_completed: Array<Int?> = arrayOfNulls(processes.size)

        for (i in processes.indices){
            burst_remaining[i] = processes[i]!!.burstTime
            is_completed[i] = 0
        }

        var current_time = 0
        var completed = 0
        var prev = 0
        val n = processes.size

        while (completed != n) {
            var idx = -1
            var mn = 10000000
            for (i in 0 until n) {
                if (processes[i]!!.arrivalTime <= current_time && is_completed[i] == 0) {
                    if (processes[i]!!.priority < mn) {
                        mn = processes[i]!!.priority
                        idx = i
                    }
                    if (burst_remaining[i] == mn) {
                        if (processes[i]!!.arrivalTime < processes[idx]!!.arrivalTime) {
                            mn = processes[i]!!.priority
                            idx = i
                        }
                    }
                }
            }
            if (idx != -1) {
                if (burst_remaining[idx] == processes[idx]!!.burstTime) {
                    processes.get(idx)!!.firstResponse = current_time
                }
                burst_remaining[idx] = burst_remaining[idx]?.minus(1)
                current_time++
                prev = current_time
                if (burst_remaining.get(idx) == 0) {
                    if (queueItems.isNotEmpty()
                        && queueItems[queueItems.size - 1].processName == processes[idx]!!.name) {
                        queueItems[queueItems.size - 1].completionTime = current_time
                    } else {
                        val chartElement = ChartElement(processes[idx]!!.name, current_time)
                        queueItems.add(chartElement)
                    }
                    val completion_time = current_time
                    val turnaround_time =
                        completion_time - processes.get(idx)!!.arrivalTime
                    val waiting_time = turnaround_time - processes.get(idx)!!.burstTime
                    val relativeDelay = turnaround_time.toDouble() / processes.get(idx)!!.burstTime
                    val response_time = processes.get(idx)!!.firstResponse - processes.get(idx)!!.arrivalTime
                    val result = Result(processes[idx]!!.name, turnaround_time,waiting_time,response_time,relativeDelay,completion_time)
                    results[idx] = result
                    is_completed[idx] = 1
                    completed++
                } else {
                    if (queueItems.isNotEmpty()
                        && queueItems[queueItems.size - 1].processName == processes[idx]!!.name) {
                        queueItems[queueItems.size - 1].completionTime = current_time
                    } else {
                        val chartElement = ChartElement(processes[idx]!!.name, current_time)
                        queueItems.add(chartElement)
                    }
                }
            } else {
                current_time++
            }
        }
        return results
    }

    fun getQueueItems(): ArrayList<ChartElement> {
        return queueItems
    }
}