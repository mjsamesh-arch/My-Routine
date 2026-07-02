package com.example.routinecheck

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Button

class RoutineAdapter(
    private val context: Context,
    private val routines: MutableList<Routine>,
    private val onChanged: () -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = routines.size
    override fun getItem(position: Int): Any = routines[position]
    override fun getItemId(position: Int): Long = routines[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_routine, parent, false)

        val routine = routines[position]

        val checkBox = view.findViewById<CheckBox>(R.id.checkDone)
        val textName = view.findViewById<TextView>(R.id.textRoutineName)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)

        textName.text = routine.name

        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = routine.done
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            routine.done = isChecked
            onChanged()
        }

        btnDelete.setOnClickListener {
            routines.removeAt(position)
            notifyDataSetChanged()
            onChanged()
        }

        return view
    }
}
