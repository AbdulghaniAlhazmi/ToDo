package com.example.todo.TaskFragment

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todo.Database.Task
import com.example.todo.DatePickerFragment
import com.example.todo.R
import com.example.todo.TaskListFragment.KEY
import com.example.todo.TaskListFragment.TaskFragmentList
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

const val Date_KEY = "Date"
const val DATE_FORMAT = "dd MMM"


class TaskFragment : Fragment(), DatePickerFragment.DatePickerCallBack {

    private lateinit var task: Task
    private lateinit var titleText: EditText
    private lateinit var startDateBtn: Button
    private lateinit var endDateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var extraInfo: EditText
    private lateinit var extraInfoBox: CheckBox
    lateinit var isDone: CheckBox

    private val taskFragmentViewModel by lazy { ViewModelProvider(this).get(TaskFragmentViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        task = Task()

        val taskId = arguments?.getSerializable(KEY) as UUID
        taskFragmentViewModel.loadTask(taskId)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_todo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.update_task -> {
                if (titleText.text.isEmpty()) {
                    Toast.makeText(context, "Enter Task Title", Toast.LENGTH_SHORT).show()
                } else {
                    taskFragmentViewModel.saveUpdate(task)
                    val fragment = TaskFragmentList()
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskFragmentViewModel.taskLiveData.observe(viewLifecycleOwner, {
            it.let {
                if (it != null) {
                    task = it
                }
                titleText.setText(it?.taskTitle)
                extraInfo.setText(it?.extraInfo)
                isDone.isChecked = it?.completed == true
                extraInfoBox.isChecked = it?.extraInfoBox == true
                startDateBtn.text = it?.startDate.toString()
                when {
                    task.startDate == task.endDate -> {
                        endDateBtn.text = "Add Due Date"
                    }
                    Calendar.getInstance().time.after(task.endDate) -> {
                        endDateBtn.text =
                            ("Overdue " + DateFormat.format(DATE_FORMAT, task.endDate).toString())
                        endDateBtn.setTextColor((Color.parseColor("#ff0000")))
                    }
                    else -> {
                        endDateBtn.text =
                            ("Due " + DateFormat.format(DATE_FORMAT, task.endDate).toString())
                    }
                }

            }
        })

        if (task.extraInfo.isNotEmpty()){
            extraInfoBox.isChecked
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo, container, false)
        init(view)


        return view
    }

    private fun init(view: View) {
        titleText = view.findViewById(R.id.task_title)
        startDateBtn = view.findViewById(R.id.start_date)
        endDateBtn = view.findViewById(R.id.end_date)
        extraInfo = view.findViewById(R.id.extra_infoText)
        extraInfoBox = view.findViewById(R.id.extra_info_box)
        deleteBtn = view.findViewById(R.id.delete_btn)
        isDone = view.findViewById(R.id.isDone)
    }

    override fun onStart() {
        super.onStart()

        val titleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.taskTitle = s.toString()

            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        titleText.addTextChangedListener(titleTextWatcher)


        isDone.setOnCheckedChangeListener { _, isChecked -> task.completed = isChecked }

        extraInfoBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                extraInfo.visibility = View.VISIBLE
            } else {
                if (extraInfo.text.isEmpty())
                    extraInfo.setText("")
                extraInfo.visibility = View.INVISIBLE
            }


            val extraInfoTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    task.extraInfo = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }

            extraInfo.addTextChangedListener(extraInfoTextWatcher)
        }

        endDateBtn.setOnClickListener {
            val args = Bundle()
            args.putSerializable(Date_KEY, task.endDate)

            val datePicker = DatePickerFragment()
            datePicker.arguments = args
            datePicker.setTargetFragment(this, 0)
            datePicker.show(this.parentFragmentManager, "Date Picker")
        }

        deleteBtn.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete ${titleText.text} Task ?")
                .setPositiveButton("YES") { dialog, which ->
                    taskFragmentViewModel.deleteTask(task)
                    val fragment = TaskFragmentList()
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
                }
                .setNeutralButton("NO") { dialog, whick -> }
                .show()

        }
    }


    override fun onDateSelected(date: Date) {
        task.endDate = date
        endDateBtn.text = DateFormat.format(DATE_FORMAT, task.endDate).toString()

        task.startDate
        startDateBtn.text = DateFormat.format(DATE_FORMAT, task.startDate).toString()
    }

    override fun onStop() {
        super.onStop()
        if (titleText.text.isEmpty()) {
            taskFragmentViewModel.deleteTask(task)
        }
        else{

        }
    }

}
