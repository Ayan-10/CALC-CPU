package com.example.cpuschedulingcalculator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cpuschedulingcalculator.algorithms.FCFS
import com.example.cpuschedulingcalculator.algorithms.PriorityScheduling
import com.example.cpuschedulingcalculator.algorithms.RoundRobin
import com.example.cpuschedulingcalculator.algorithms.SJF
import com.example.cpuschedulingcalculator.databinding.FragmentFirstBinding
import com.example.cpuschedulingcalculator.model.ChartElement
import com.example.cpuschedulingcalculator.model.Process
import com.example.cpuschedulingcalculator.model.Result

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class InputScreen : Fragment() {

    private var notInput: Boolean = false
    private var _binding: FragmentFirstBinding? = null

    private lateinit var processTable: TableLayout
    private lateinit var algorithm: Spinner
    private lateinit var algoSubclass: RadioGroup
    private lateinit var  emptive: RadioButton
    private lateinit var  nonEmptive: RadioButton

    private lateinit var rows: MutableList<TableRow>
    private lateinit var algorithms: Array<String>
    private lateinit var processes: Array<Process?>
    private lateinit var results: Array<Result?>
    private lateinit var queueItems: ArrayList<ChartElement>


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        initViews()
        initVariables()
        return binding.root

    }

    private fun initVariables() {
        rows = ArrayList<TableRow>()

    }

    private fun initViews() {
        processTable = binding.tableLayout
        algorithm = binding.algorithm
        algoSubclass = binding.algoSubclass
        emptive = binding.emptive
        nonEmptive = binding.nonEmptive
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.add.setOnClickListener {
            val newRow = LayoutInflater.from(requireContext())
                .inflate(R.layout.input_row, null) as TableRow
            processTable.addView(newRow)
            rows.add(newRow)
            (newRow.findViewById<View>(R.id.pid) as TextView).text =
                "p" + (processTable.getChildCount() - 1)
        }

        binding.remove.setOnClickListener(View.OnClickListener {
            val len = processTable.childCount
            if (len > 2) processTable.removeViewAt(len - 1)
            else Toast.makeText(
                requireContext(),
                "At least one row required",
                Toast.LENGTH_LONG
            ).show()
        })

        algorithms = arrayOf("Select algorithm", "FCFS", "SJF", "Priority", "Round robin")

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.spinner_item, algorithms)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        algorithm.adapter = adapter

        binding.calculate.setOnClickListener {
            val type: Int = algorithm.selectedItemPosition
            if ((type == 4 && binding.quantum.getText().isNullOrBlank())){
                Toast.makeText(requireContext(), "Please enter quantum time in integer", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (type == 0) {
                Toast.makeText(requireContext(), "Please select algorithm type", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            fetchData()
            if (notInput == true) {
                Toast.makeText(requireContext(), "Row can't be empty", Toast.LENGTH_LONG)
                    .show()
                notInput = false
                return@setOnClickListener
            }
            getResult()
            val bundle = Bundle()
            bundle.putParcelableArray("processes", processes)
            bundle.putParcelableArray("key", results)
            bundle.putParcelableArrayList("chart", queueItems)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }
    }

    private fun getResult() {
        val type: Int = algorithm.selectedItemPosition
        val len = processTable.childCount
        results = arrayOfNulls(len - 1)
        val choice: Int
        when (type) {
            0 -> {
                Toast.makeText(requireContext(), "Please select algorithm type", Toast.LENGTH_LONG)
                    .show()
            }
            1 -> {
                val fcfs = FCFS()
                results = fcfs.getResult(processes)
                queueItems = fcfs.getQueueItems()
            }
            2 -> {
                val sjf = SJF()
                choice = algoSubclass.getCheckedRadioButtonId()
                results = if (choice == R.id.non_emptive) {
                    sjf.getNonPreemptiveResult(processes)
                }else {
                    sjf.getPreemptiveResult(processes)
                }
//                cpuQueue = sjf.getCpuQueue()

                queueItems = sjf.getQueueItems()
            }
            3 -> {
                val priorityScheduling = PriorityScheduling()
                choice = algoSubclass.getCheckedRadioButtonId()
                results = if (choice == R.id.non_emptive) {
                    priorityScheduling.getNonPreemptiveResult(processes)
                }else {
                    priorityScheduling.getPreemptiveResult(processes)
                }

                queueItems = priorityScheduling.getQueueItems()
            }
            4 -> {
                val roundRobin = RoundRobin()
                try {
                    results = roundRobin.getResult(processes, binding.quantum.getText().toString().toInt())
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Please enter quantum time in integer", Toast.LENGTH_LONG)
                        .show()
                    return
                }
                queueItems = roundRobin.getQueueItems()
            }
        }
        Log.d("haha", "getResult: "+results)
    }

    private fun fetchData() {
        val type: Int = algorithm.selectedItemPosition
        if (type == 0) {
            Toast.makeText(requireContext(), "Please select algorithm type", Toast.LENGTH_LONG)
                .show()
        } else {
            val len = processTable.childCount
            processes = arrayOfNulls(len - 1)

            for (i in 1 until len) {
                val row = processTable.getChildAt(i) as TableRow
                val processName = (row.findViewById<View>(R.id.pid) as TextView).text.toString()
                if (processName.compareTo("") == 0) {
                    Toast.makeText(requireContext(), "Please enter process name", Toast.LENGTH_LONG).show()
                    return
                }
                if((row.findViewById<View>(R.id.atime)
                            as EditText).text.isNullOrBlank()
                    || (row.findViewById<View>(R.id.btime)
                            as EditText).text.isNullOrBlank()
                    || (row.findViewById<View>(R.id.priority)
                            as EditText).text.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "Row can't be empty", Toast.LENGTH_LONG).show()
                    notInput = true
                    return
                }
                val arrivalTime = (row.findViewById<View>(R.id.atime)
                        as EditText).text.toString().toInt()
                val burstTime = (row.findViewById<View>(R.id.btime)
                        as EditText).text.toString().toInt()
                val priority = (row.findViewById<View>(R.id.priority)
                        as EditText).text.toString().toInt()
                val process = Process(processName, arrivalTime, burstTime, priority)
                processes[i - 1] = process
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}