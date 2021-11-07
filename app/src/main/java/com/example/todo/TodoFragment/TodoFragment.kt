package com.example.todo.TodoFragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.todo.Database.Todo
import com.example.todo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TodoFragment : Fragment() {

    private lateinit var todo: Todo
    private lateinit var titleText : EditText
    private lateinit var startDate : Button
    private lateinit var endDate : Button
    private lateinit var extraInfo : EditText
    private lateinit var extraInfoBox : CheckBox
    private lateinit var isDone : FloatingActionButton

private val todoFragmentViewModel by lazy { ViewModelProvider(this).get(TodoFragmentViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todo = Todo()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo, container, false)
        titleText = view.findViewById(R.id.todo_title)
        startDate = view.findViewById(R.id.start_date)
        endDate = view.findViewById(R.id.end_date)
        extraInfo = view.findViewById(R.id.extra_infoText)
        extraInfoBox = view.findViewById(R.id.extra_info_box)



        return view
    }

    override fun onStart() {
        super.onStart()

        extraInfoBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                extraInfo.visibility = View.VISIBLE
            }
            if (!isChecked){
                extraInfo.setText("")
                extraInfo.visibility = View.INVISIBLE
            }
        }

    }

}