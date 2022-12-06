package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.MainViewModel
import com.example.todolist.databinding.CardLayoutBinding
import com.example.todolist.model.Task

class TaskAdapter(
    val taskClickListener: TaskClickListener,
    val mainViewModel: MainViewModel
    ): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList = emptyList<Task>()

    class TaskViewHolder (val binding: CardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(CardLayoutBinding.inflate(LayoutInflater.from(parent.context),
        parent, false
        ))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.binding.textNome.text = task.name
        holder.binding.textDescricao.text = task.description
        holder.binding.textResponsavel.text = task.responsavel
        holder.binding.textData.text = task.date
        holder.binding.switch1.isChecked = task.status
        holder.binding.textCategoria.text = task.category.description

        holder.itemView.setOnClickListener {
            taskClickListener.onTaskClickLister(task)
        }

        holder.binding.switch1
            .setOnCheckedChangeListener { compoundButton, ativo ->
                task.status = ativo
                mainViewModel.taskUpdate(task)
            }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun setList(list: List<Task>){
        taskList = list.sortedByDescending { it.id }
        notifyDataSetChanged()
    }
}