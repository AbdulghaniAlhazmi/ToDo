package com.example.todo.TaskListFragment

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.CompletedFragment.CompletedFragment
import com.example.todo.Database.Task
import com.example.todo.R
import com.example.todo.TaskFragment.DATE_FORMAT
import com.example.todo.TaskFragment.TaskFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


const val KEY = "1"

var update: Boolean = false

class TaskFragmentList : Fragment() {
    private lateinit var taskRcView: RecyclerView
    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProvider(this).get(TaskListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.new_task -> {
                val task = Task()
                taskListViewModel.addTask(task)
                val fragment = TaskFragment()
                val args = Bundle()
                args.putSerializable(KEY, task.id)
                fragment.arguments = args
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
                true
            }
            R.id.completed_tasks -> {
                val fragment = CompletedFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()

                true
            }
            R.id.delete_all -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete All Tasks ?")
                    .setPositiveButton("YES") { dialog, which ->
                        taskListViewModel.deleteAll()
                    }
                    .setNeutralButton("NO") { dialog, whick -> }
                    .show()
                true
            }
            R.id.delete_incomplete -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete All Incomplete Tasks ?")
                    .setPositiveButton("YES") { dialog, which ->
                        taskListViewModel.deleteIncomplete()
                    }
                    .setNeutralButton("NO") { dialog, whick -> }
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo_list, container, false)

        taskRcView = view.findViewById(R.id.todo_rc_view)
        taskRcView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListViewModel.taskLiveData.observe(viewLifecycleOwner, {
            updateUI(it)
        })

    }

    private fun updateUI(tasks: List<Task>) {
        adapter = TaskAdapter(tasks)
        taskRcView.adapter = adapter
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_todo_list, menu)
    }


    private inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var task: Task
        private val isDoneBox: CheckBox = itemView.findViewById(R.id.isDone)
        private val taskTitle: TextView = itemView.findViewById(R.id.task_title)
        private val dueDate: Button = itemView.findViewById(R.id.due_date)

        init {

            itemView.setOnClickListener(this)

            isDoneBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    taskTitle.paintFlags = taskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    dueDate.visibility = View.INVISIBLE
                    itemView.visibility = View.GONE
                } else {
                    taskTitle.paintFlags =
                        taskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    dueDate.visibility = View.VISIBLE
                    itemView.visibility = View.VISIBLE

                }
            }

        }

        fun bind(task: Task) {
            this.task = task
            taskTitle.text = this.task.taskTitle
            isDoneBox.isChecked = task.completed
            dueDate.text = DateFormat.format(DATE_FORMAT, task.endDate).toString()

            isDoneBox.setOnCheckedChangeListener(fun(_: CompoundButton, _: Boolean) {
                if (isDoneBox.isChecked) {
                    taskListViewModel.updateCompleted(true, task.id)
                    Toast.makeText(
                        requireContext(),
                        "Task ${taskTitle.text} Completed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    taskListViewModel.updateCompleted(false, task.id)
                }
            })


            if (task.startDate == task.endDate) {
                dueDate.visibility = View.INVISIBLE
            } else {
                if (Calendar.getInstance().time.after(task.endDate)) {
                    dueDate.setTextColor(Color.parseColor("#A70707"))

                } else {
                    dueDate.setTextColor(Color.parseColor("#000000"))
                }
            }
        }

        override fun onClick(v: View?) {
            val args = Bundle()
            args.putSerializable(KEY, task.id)

            if (v == itemView) {
                val fragment = TaskFragment()
                fragment.arguments = args
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)
                    ?.addToBackStack(null)?.commit()
            }
        }
    }


    private inner class TaskAdapter(var tasks: List<Task>) : RecyclerView.Adapter<TaskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
            val view = layoutInflater.inflate(R.layout.list_todo, parent, false)
            return TaskHolder(view)
        }

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

        override fun getItemCount() = tasks.size
    }


    override fun onStop() {
        super.onStop()

        Log.d("TTTT", update.toString())

    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("TTTT", update.toString())
    }

}