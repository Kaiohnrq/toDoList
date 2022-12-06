package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.adapter.TaskAdapter
import com.example.todolist.adapter.TaskClickListener
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.model.Task

class ListFragment : Fragment(), TaskClickListener {

    private lateinit var binding: FragmentListBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    binding = FragmentListBinding.inflate(layoutInflater, container, false)
    mainViewModel.taskList()


    val adapter = TaskAdapter(this, mainViewModel)
    binding.recyclerTarefa.layoutManager = LinearLayoutManager(context)
    binding.recyclerTarefa.adapter = adapter
    binding.recyclerTarefa.setHasFixedSize(true)


    binding.floatingActionButton.setOnClickListener {
        mainViewModel.selecTask = null
        findNavController().navigate(R.id.action_listFragment_to_formFragment)
    }

    mainViewModel.myResponseTask.observe(viewLifecycleOwner){

        response -> if(response.body() != null){

            adapter.setList(response.body()!!)

    }

    }

    return binding.root
    }

    override fun onTaskClickLister(task: Task) {

        mainViewModel.selecTask = task
        findNavController().navigate(R.id.action_listFragment_to_formFragment)

    }

}