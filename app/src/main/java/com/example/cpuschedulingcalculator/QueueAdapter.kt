package com.example.cpuschedulingcalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cpuschedulingcalculator.model.ChartElement
import com.example.cpuschedulingcalculator.model.Result
import java.util.ArrayList


class QueueAdapter(
    private val context: Context,
    private val results: ArrayList<ChartElement>

) : RecyclerView.Adapter<QueueAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.queue_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = results[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.text = item.processName

        // sets the text to the textview from our itemHolder class
        holder.textView.text = item.completionTime.toString()

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return results.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: TextView = itemView.findViewById(R.id.ps)
        val textView: TextView = itemView.findViewById(R.id.ct)
    }

}