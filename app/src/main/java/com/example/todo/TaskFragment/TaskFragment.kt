package com.example.todo.TaskFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.todo.Database.Task
import com.example.todo.DatePickerFragment
import com.example.todo.R
import com.example.todo.TaskListFragment.KEY
import java.util.*

const val Date_KEY = "Date"

class TaskFragment : Fragment(), DatePickerFragment.DatePickerCallBack {

    private lateinit var task: Task
    private lateinit var titleText: EditText
    private lateinit var startDateBtn: Button
    private lateinit var endDateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var extraInfo: EditText
    private lateinit var extraInfoBox: CheckBox


    private val taskFragmentViewModel by lazy { ViewModelProvider(this).get(TaskFragmentViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()

        val taskId = arguments?.getSerializable(KEY) as UUID
        taskFragmentViewModel.loadTask(taskId)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskFragmentViewModel.taskLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                if (it != null) {
                    task = it
                }
                titleText.setText(it?.taskTitle)
                extraInfo.setText(it?.extraInfo)
                startDateBtn.text = it?.startDate.toString()
                endDateBtn.text = it?.endDate.toString()
            }
        })
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
        startDateBtn.apply {
            text = task.startDate.toString()
        }
        endDateBtn.apply {
            text = task.startDate.toString()
        }
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

        extraInfoBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                extraInfo.visibility = View.VISIBLE
            }
            if (!isChecked) {
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
            args.putSerializable(Date_KEY, task.startDate)

            val datePicker = DatePickerFragment()
            datePicker.arguments = args
            datePicker.setTargetFragment(this, 0)
            datePicker.show(this.parentFragmentManager, "Date Picker")
        }

        deleteBtn.setOnClickListener {

            taskFragmentViewModel.deleteTask(task)

        }

    }

    override fun onStop() {
        super.onStop()
        taskFragmentViewModel.saveUpdate(task)
    }


    override fun onDateSelected(date: Date) {
        task.endDate = date
        endDateBtn.text = date.toString()

    }

}