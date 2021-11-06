package com.example.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.todo.Database.Todo


class TodoFragment : Fragment() {

    private lateinit var todo: Todo
    private lateinit var titleText : EditText
    private lateinit var startDate : Button
    private lateinit var endDate : Button
    private lateinit var extraInfo : EditText
    private lateinit var extraInfoBox : CheckBox
    private lateinit var isDone : CheckBox



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todo = Todo()
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
        isDone = view.findViewById(R.id.is_done)



        return view
    }

    override fun onStart() {
        super.onStart()

        extraInfoBox.setOnClickListener{
            if (extraInfoBox.isEnabled){
                extraInfo.visibility = View.VISIBLE
            }
        }

    }

}