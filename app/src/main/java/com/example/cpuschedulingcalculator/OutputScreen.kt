package com.example.cpuschedulingcalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cpuschedulingcalculator.databinding.FragmentSecondBinding
import com.example.cpuschedulingcalculator.model.ChartElement
import com.example.cpuschedulingcalculator.model.Process
import com.example.cpuschedulingcalculator.model.Result
import java.util.ArrayList
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class OutputScreen : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private lateinit var processTable: TableLayout
    private lateinit var processDetails: TableLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        initViews()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val results: Array<Result> = arguments?.getParcelableArray("key") as Array<Result>
        val processes: Array<Process> = arguments?.getParcelableArray("processes") as Array<Process>
        val chartElements: ArrayList<ChartElement> = arguments?.getParcelableArrayList<ChartElement>("chart")!!
        var avgTat: Int = 0
        var avgWt: Int = 0
        var avgRt: Int = 0
        var avgRd: Double = 0.0

        for (i in processes!!.indices) {
            val newRow = LayoutInflater.from(requireContext())
                .inflate(R.layout.input_row, null) as TableRow
            processDetails.addView(newRow)
            (newRow.findViewById<View>(R.id.pid) as TextView).text =
                processes[i].name
            (newRow.findViewById<View>(R.id.atime) as TextView).text =
                processes[i].arrivalTime.toString()
            (newRow.findViewById<View>(R.id.btime) as TextView).text =
                processes[i].burstTime.toString()
            (newRow.findViewById<View>(R.id.priority) as TextView).text =
                processes[i].priority.toString()
        }

        for (i in results!!.indices) {
            val newRow = LayoutInflater.from(requireContext())
                .inflate(R.layout.output_row, null) as TableRow
            processTable.addView(newRow)
            (newRow.findViewById<View>(R.id.pid) as TextView).text =
                results[i].processName
            avgTat += results[i].turnAroundTime
            (newRow.findViewById<View>(R.id.tat) as TextView).text =
                results[i].turnAroundTime.toString()
            avgWt += results[i].waitingTime
            (newRow.findViewById<View>(R.id.wt) as TextView).text =
                results[i].waitingTime.toString()
            avgRt += results[i].responseTime
            (newRow.findViewById<View>(R.id.rt) as TextView).text =
                results[i].responseTime.toString()
            avgRd += results[i].relativeDelay
            (newRow.findViewById<View>(R.id.rd) as TextView).text =
                String.format("%.2f", results[i].relativeDelay)
        }

        binding.avgTat.text = String.format("%.2f", (avgTat.toDouble() / results.size))
        binding.avgWt.text = String.format("%.2f", (avgWt.toDouble() / results.size))
        binding.avgRt.text = String.format("%.2f", (avgRt.toDouble() / results.size))
        binding.avgRd.text = String.format("%.2f", (avgRd.toDouble() / results.size))


        // getting the recyclerview by its id
        val recyclerview = binding.rv

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.HORIZONTAL, false)

        results.sortBy { result -> result.completionTime }

        // This will pass the ArrayList to our Adapter
        val adapter = QueueAdapter(requireContext(), chartElements)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun initViews() {
        processTable = binding.tableLayout
        processDetails = binding.processTableLayout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}