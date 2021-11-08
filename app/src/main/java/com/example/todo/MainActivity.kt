package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo.TaskFragment.TaskFragment
import com.example.todo.TaskListFragment.TaskFragmentList
import com.example.todo.TaskListFragment.TaskListViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = TaskFragmentList()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()

        }
    }
}