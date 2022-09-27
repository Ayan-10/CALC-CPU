package com.example.cpuschedulingcalculator.algorithms

import com.example.cpuschedulingcalculator.model.ChartElement
import com.example.cpuschedulingcalculator.model.Process
import com.example.cpuschedulingcalculator.model.Result

class FCFS {

    private lateinit var results : Array<Result?>
    private var queueItems: ArrayList<ChartElement> = ArrayList()

    fun getResult(processes: Array<Process?>): Array<Result?> {
        results = arrayOfNulls(processes.size)
        processes.sortBy { process -> process!!.arrivalTime }

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

    fun getQueueItems(): ArrayList<ChartElement> {
        return queueItems
    }
}