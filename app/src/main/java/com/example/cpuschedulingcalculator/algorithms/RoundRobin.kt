package com.example.cpuschedulingcalculator.algorithms

import android.util.Log
import com.example.cpuschedulingcalculator.model.ChartElement
import com.example.cpuschedulingcalculator.model.Process
import com.example.cpuschedulingcalculator.model.Result
import java.util.*
import kotlin.collections.ArrayList

class RoundRobin {
    private lateinit var results : Array<Result?>
    private var queueItems: ArrayList<ChartElement> = ArrayList()

    fun getResult(processes: Array<Process?>, q: Int): Array<Result?> {
        val queue: Queue<Int> = LinkedList<Int>()
        results = arrayOfNulls(processes.size)
        processes.sortBy { process -> process!!.arrivalTime }
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

            if (queue.isEmpty()) {
                var found = false
                while (found == false) {
                    for (i in 0 until n) {
                        if (processes[i]!!.arrivalTime <= current_time
                            && burst_remaining[i] == processes[i]!!.burstTime && !queue.contains(i)) {
                            found = true
                            queue.add(i)
                            Log.d("haha", "getResult: "+i)
                        }
                    }
                    Log.d("haha", "getResult: "+" next")

                    if (found == true) {
                        break
                    } else {
                        current_time++
                    }
                }

            }

            while (queue.isNotEmpty()) {

                val i = queue.remove()

                if (burst_remaining[i]!! > 0) {
                    if (burst_remaining[i]!! > q)
                    {
                        // Increase the value of t to see the time for which a process has been processed

                        if (burst_remaining[i] == processes[i]!!.burstTime) {
                            processes[i]!!.firstResponse = current_time
                        }


                        // Reduce the burst_time of running process by quantum
                        burst_remaining[i] = burst_remaining[i]?.minus(q);

                        if (burst_remaining[i]!! == 0){
                            val completion_time = current_time
                            val turnaround_time =
                                completion_time - processes[i]!!.arrivalTime
                            val waiting_time = turnaround_time - processes[i]!!.burstTime
                            val relativeDelay = turnaround_time.toDouble() / processes[i]!!.burstTime
                            val response_time = processes[i]!!.firstResponse - processes.get(i)!!.arrivalTime
                            val result = Result(processes[i]!!.name, turnaround_time,waiting_time,response_time,relativeDelay,completion_time)
                            results[i] = result
                            is_completed[i] = 1
                            completed++
                            current_time += q;
                            for (idx in 0 until n) {
                                if (processes[idx]!!.arrivalTime <= current_time
                                    && burst_remaining[idx] == processes[idx]!!.burstTime
                                    && !queue.contains(idx)) {
                                    queue.add(idx)
                                    Log.d("haha", "getResult: "+idx)
                                }
                            }
                            Log.d("haha", "getResult: "+" next")

                        } else {
                            current_time += q;
                            for (idx in 0 until n) {
                                if (processes[idx]!!.arrivalTime <= current_time
                                    && burst_remaining[idx] == processes[idx]!!.burstTime
                                    && !queue.contains(idx)) {
                                    queue.add(idx)
                                    Log.d("haha", "getResult: "+idx)
                                }
                            }
                            Log.d("haha", "getResult: "+" next")

                            queue.add(i)
                            Log.d("haha", "getResult: "+i)

                        Log.d("haha", "getResult: "+" next")
                        }

                        if (queueItems.isNotEmpty()
                            && queueItems[queueItems.size - 1].processName == processes[i]!!.name) {
                            queueItems[queueItems.size - 1].completionTime = current_time
                        } else {
                            val chartElement = ChartElement(processes[i]!!.name, current_time)
                            queueItems.add(chartElement)
                        }
                    }

                    // If burst time is <= q, last cycle of this process
                    else
                    {
                        if (burst_remaining[i] == processes[i]!!.burstTime) {
                            processes[i]!!.firstResponse = current_time
                        }
                        // Increase the value of t
                        current_time += burst_remaining[i]!!
                        for (idx in 0 until n) {
                            if (processes[idx]!!.arrivalTime <= current_time
                                && burst_remaining[idx] == processes[idx]!!.burstTime
                                && !queue.contains(idx)) {
                                queue.add(idx)
                                Log.d("haha", "getResult: "+idx)
                            }

                        }
                        Log.d("haha", "getResult: "+" next")

//                        // Waiting time = current time - time used by the process
//                        wt[i] = t - bt[i];
//
//                        // As the process gets over, its remaining burst time = 0
                        burst_remaining[i] = 0
                        is_completed[i] = 1
                        completed++

                        val completion_time = current_time
                        val turnaround_time =
                            completion_time - processes[i]!!.arrivalTime
                        val waiting_time = turnaround_time - processes[i]!!.burstTime
                        val relativeDelay = turnaround_time.toDouble() / processes[i]!!.burstTime
                        val response_time = processes[i]!!.firstResponse - processes.get(i)!!.arrivalTime
                        val result = Result(processes[i]!!.name, turnaround_time,waiting_time,response_time,relativeDelay,completion_time)
                        results[i] = result

                        if (queueItems.isNotEmpty()
                            && queueItems[queueItems.size - 1].processName == processes[i]!!.name) {
                            queueItems[queueItems.size - 1].completionTime = current_time
                        } else {
                            val chartElement = ChartElement(processes[i]!!.name, current_time)
                            queueItems.add(chartElement)
                        }
                    }
                }

            }

//            for (i in 0 until n) {
//
//                while (processes[i]!!.arrivalTime > current_time){
//                    current_time++
//                }
//
//                if (burst_remaining[i]!! > 0) {
//                    if (burst_remaining[i]!! > q)
//                    {
//                        // Increase the value of t to see the time for which a process has been processed
//
//                        if (burst_remaining[i] == processes[i]!!.burstTime) {
//                            processes[i]!!.firstResponse = current_time
//                        }
//
//                        current_time += q;
//                        // Reduce the burst_time of running process by quantum
//                        burst_remaining[i] = burst_remaining[i]?.minus(q);
//
//                        if (burst_remaining[i]!! == 0){
//                            val completion_time = current_time
//                            val turnaround_time =
//                                completion_time - processes[i]!!.arrivalTime
//                            val waiting_time = turnaround_time - processes[i]!!.burstTime
//                            val relativeDelay = turnaround_time.toDouble() / processes[i]!!.burstTime
//                            val response_time = processes[i]!!.firstResponse - processes.get(i)!!.arrivalTime
//                            val result = Result(processes[i]!!.name, turnaround_time,waiting_time,response_time,relativeDelay,completion_time)
//                            results[i] = result
//                            is_completed[i] = 1
//                            completed++
//                        }
//
//                        if (queueItems.isNotEmpty()
//                            && queueItems[queueItems.size - 1].processName == processes[i]!!.name) {
//                            queueItems[queueItems.size - 1].completionTime = current_time
//                        } else {
//                            val chartElement = ChartElement(processes[i]!!.name, current_time)
//                            queueItems.add(chartElement)
//                        }
//                    }
//
//                    // If burst time is <= q, last cycle of this process
//                    else
//                    {
//                        if (burst_remaining[i] == processes[i]!!.burstTime) {
//                            processes[i]!!.firstResponse = current_time
//                        }
//                        // Increase the value of t
//                        current_time += burst_remaining[i]!!
//
////                        // Waiting time = current time - time used by the process
////                        wt[i] = t - bt[i];
////
////                        // As the process gets over, its remaining burst time = 0
//                        burst_remaining[i] = 0
//                        is_completed[i] = 1
//                        completed++
//
//                        val completion_time = current_time
//                        val turnaround_time =
//                            completion_time - processes[i]!!.arrivalTime
//                        val waiting_time = turnaround_time - processes[i]!!.burstTime
//                        val relativeDelay = turnaround_time.toDouble() / processes[i]!!.burstTime
//                        val response_time = processes[i]!!.firstResponse - processes.get(i)!!.arrivalTime
//                        val result = Result(processes[i]!!.name, turnaround_time,waiting_time,response_time,relativeDelay,completion_time)
//                        results[i] = result
//
//                        if (queueItems.isNotEmpty()
//                            && queueItems[queueItems.size - 1].processName == processes[i]!!.name) {
//                            queueItems[queueItems.size - 1].completionTime = current_time
//                        } else {
//                            val chartElement = ChartElement(processes[i]!!.name, current_time)
//                            queueItems.add(chartElement)
//                        }
//                    }
//                }
//            }
        }
        return results
    }

    fun getQueueItems(): ArrayList<ChartElement> {
        return queueItems
    }
}