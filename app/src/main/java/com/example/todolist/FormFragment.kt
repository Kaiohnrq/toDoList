package com.example.todolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentFormBinding
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.fragment.DatePickerFragment
import com.example.todolist.fragment.TimerPickerListener
import com.example.todolist.model.Categoria
import com.example.todolist.model.Tarefa
import java.time.LocalDate

class FormFragment : Fragment(), TimerPickerListener {

    private var tarefaSelecionada: Tarefa?= null
    private lateinit var binding: FragmentFormBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var categoriaSelecionada = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    binding = FragmentFormBinding.inflate(layoutInflater, container, false)

    carregarDados()

    mainViewModel.listCategoria()
    mainViewModel.dataSelecionada.value = LocalDate.now()
    mainViewModel.dataSelecionada.observe(viewLifecycleOwner){

        selectDate -> binding.editDate.setText(selectDate.toString())

    }

    mainViewModel.myCategoriaResponse.observe(viewLifecycleOwner){

        response -> Log.d("Requisicao", response.body().toString())
        spinnerCategoria(response.body())

    }

    binding.buttonSalvar.setOnClickListener {

            inserirNoBanco()

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

    private fun spinnerCategoria(listCategoria: List<Categoria>?){

        if(listCategoria != null){

            binding.spinnerCategoria.adapter = ArrayAdapter(

                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                listCategoria

            )

            binding.spinnerCategoria.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val selected = binding.spinnerCategoria.selectedItem as Categoria

                    categoriaSelecionada = selected.id

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

    private fun validarCampos ( nome: String, descricao: String, responsavel: String ) :
    Boolean {

        return !(
                ( nome == "" || nome.length < 3 || nome.length > 20) ||
                ( descricao == "" || descricao.length < 5 || descricao.length > 200) ||
                ( responsavel == "" || responsavel.length < 3 || responsavel.length > 20 )
                )

    }

    private fun inserirNoBanco () {

        val nome = binding.editNome.text.toString()
        val desc = binding.editDescricao.text.toString()
        val responsavel = binding.editResponsavel.text.toString()
        val data = binding.editDate.text.toString()
        val status = binding.switchAtivoCard.isChecked
        val categoria = Categoria(categoriaSelecionada, null, null)

        if(validarCampos(nome, desc, responsavel)) {

            val salvar: String

            if( tarefaSelecionada != null ){

                salvar = "Tarefa Atualizada!"
                val tarefa = Tarefa(tarefaSelecionada?.id!!, nome, desc, responsavel, data, status, categoria)
                mainViewModel.addTarefa(tarefa)

            } else {

                salvar = "Tarefa Criada!"
                val tarefa = Tarefa(0, nome, desc, responsavel, data, status, categoria)
                mainViewModel.addTarefa(tarefa)

            }

                Toast.makeText(context, salvar, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.listFragment)

        } else {

            Toast.makeText(context, "Verifique os campos!", Toast.LENGTH_SHORT).show()

        }

    }

    private fun carregarDados(){

        tarefaSelecionada = mainViewModel.tarefaSeleciona
        if( tarefaSelecionada != null ){
            binding.editNome.setText(tarefaSelecionada?.nome)
            binding.editDescricao.setText(tarefaSelecionada?.descricao)
            binding.editResponsavel.setText(tarefaSelecionada?.responsavel)
            binding.editDate.setText(tarefaSelecionada?.data)
            binding.switchAtivoCard.isChecked = tarefaSelecionada?.status!!
        }

    }

    override fun onDateSelected(date: LocalDate) {

        mainViewModel.dataSelecionada.value = date

    }

}