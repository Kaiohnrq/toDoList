package com.example.todolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentFormBinding
import com.example.todolist.fragment.DatePickerFragment
import com.example.todolist.fragment.TimerPickerListener
import com.example.todolist.model.Category
import com.example.todolist.model.Task
import java.time.LocalDate

class FormFragment : Fragment(), TimerPickerListener {

    private var selectedTask: Task?= null
    private lateinit var binding: FragmentFormBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var selectedCategory = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    binding = FragmentFormBinding.inflate(layoutInflater, container, false)

    dataLoad()

    mainViewModel.categoryList()
    mainViewModel.selectedDate.value = LocalDate.now()
    mainViewModel.selectedDate.observe(viewLifecycleOwner){

        selectDate -> binding.editDate.setText(selectDate.toString())

    }

    mainViewModel.myResponseCategory.observe(viewLifecycleOwner){

        response -> Log.d("Requisicao", response.body().toString())
        spinnerCategoria(response.body())

    }

    binding.buttonSalvar.setOnClickListener {

            dataInsert()

    }

    binding.buttonCancelar.setOnClickListener {

            findNavController().navigate(R.id.listFragment)

        }

    binding.editDate.setOnClickListener {

        DatePickerFragment(this)
            .show(parentFragmentManager, "DatePicker")

    }


    return binding.root

    }

    private fun spinnerCategoria(categoryList: List<Category>?){

        if(categoryList != null){

            binding.spinnerCategoria.adapter = ArrayAdapter(

                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                categoryList

            )

            binding.spinnerCategoria.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val selected = binding.spinnerCategoria.selectedItem as Category

                    selectedCategory = selected.id

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

    private fun fieldValidator ( name: String, description: String, responsavel: String ) :
    Boolean {

        return !(
                ( name == "" || name.length < 3 || name.length > 20) ||
                ( description == "" || description.length < 5 || description.length > 200) ||
                ( responsavel == "" || responsavel.length < 3 || responsavel.length > 20 )
                )

    }

    private fun dataInsert () {

        val name = binding.editNome.text.toString()
        val desc = binding.editDescricao.text.toString()
        val responsavel = binding.editResponsavel.text.toString()
        val date = binding.editDate.text.toString()
        val status = binding.switchAtivoCard.isChecked
        val category = Category(selectedCategory, null, null)

        if(fieldValidator(name, desc, responsavel)) {

            val salvar: String

            if( selectedTask != null ){

                salvar = "Tarefa Atualizada!"
                val task = Task(selectedTask?.id!!, name, desc, responsavel, date, status, category)
                mainViewModel.taskAdd(task)

            } else {

                salvar = "Tarefa Criada!"
                val task = Task(0, name, desc, responsavel, date, status, category)
                mainViewModel.taskAdd(task)

            }

                Toast.makeText(context, salvar, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.listFragment)

        } else {

            Toast.makeText(context, "Verifique os campos!", Toast.LENGTH_SHORT).show()

        }

    }

    private fun dataLoad(){

        selectedTask = mainViewModel.selecTask
        if( selectedTask != null ){
            binding.editNome.setText(selectedTask?.name)
            binding.editDescricao.setText(selectedTask?.description)
            binding.editResponsavel.setText(selectedTask?.responsavel)
            binding.editDate.setText(selectedTask?.date)
            binding.switchAtivoCard.isChecked = selectedTask?.status!!
        }

    }

    override fun onDateSelected(date: LocalDate) {

        mainViewModel.selectedDate.value = date

    }

}