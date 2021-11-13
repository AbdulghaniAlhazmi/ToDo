package com.example.todo.CompletedFragment

import android.graphics.Paint
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.Database.Task
import com.example.todo.R
import com.example.todo.TaskFragment.DATE_FORMAT
import com.example.todo.TaskFragment.TaskFragment
import com.example.todo.TaskListFragment.KEY
import com.example.todo.TaskListFragment.TaskFragmentList
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CompletedFragment : Fragment() {

    private lateinit var taskRcView: RecyclerView
    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    private val completedViewModel: CompletedFragmentViewModel by lazy {
        ViewModelProvider(this).get(CompletedFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_completed, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.completed_tasks -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete All Completed Tasks ?")
                    .setPositiveButton("YES") { dialog, which ->
                        completedViewModel.deleteCompleted()
                    }
                    .setNeutralButton("NO") { dialog, whick -> }
                    .show()

                val fragment = TaskFragmentList()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
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
        completedViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            {
                updateUI(it)
            }
        )

    }

    private fun updateUI(tasks: List<Task>) {
        adapter = TaskAdapter(tasks)
        taskRcView.adapter = adapter
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
                    itemView.visibility = View.VISIBLE
                } else {
                    taskTitle.paintFlags =
                        taskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    dueDate.visibility = View.VISIBLE
                    itemView.visibility = View.INVISIBLE

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
                    completedViewModel.updateCompleted(true, task.id)
                } else {
                    completedViewModel.updateCompleted(false, task.id)
                }
            })

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


    private inner class TaskAdapter(var tasks: List<Task>) :
        RecyclerView.Adapter<TaskHolder>() {

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
}
