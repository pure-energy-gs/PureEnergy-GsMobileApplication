package br.com.fiap.pureenergy

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase

class RegistrarComodoFragment : Fragment(R.layout.fragment_registrar_comodo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val btnBack = view.findViewById<View>(R.id.btnBack)
        val btnAdicionarComodo = view.findViewById<Button>(R.id.btnAdicionarComodo)
        val inputNomeComodo = view.findViewById<EditText>(R.id.inputNomeComodo)
        val inputUtilidadeComodo = view.findViewById<EditText>(R.id.inputUtilidadeComodo)

        // Configuração do botão de voltar
        btnBack.setOnClickListener {
            findNavController().navigateUp() // Voltar para a tela anterior
        }

        // Configuração do botão "Adicionar Cômodo"
        btnAdicionarComodo.setOnClickListener {
            val nomeComodo = inputNomeComodo.text.toString().trim()
            val descricaoComodo = inputUtilidadeComodo.text.toString().trim()

            if (nomeComodo.isEmpty() || descricaoComodo.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                adicionarComodo(nomeComodo, descricaoComodo)
            }
        }

        // Configurar navegação no menu
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.action_registrarComodoFragment_to_homeFragment)
                    true
                }
                R.id.nav_dashboard -> {
                    Toast.makeText(requireContext(), "Dashboard em breve!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(requireContext(), "Perfil em breve!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_register_room -> {
                    true
                }
                R.id.nav_register_device -> {
                    findNavController().navigate(R.id.action_registrarComodoFragment_to_registrarAparelhoFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun adicionarComodo(nomeComodo: String, descricao: String) {
        val database = FirebaseDatabase.getInstance().getReference("Comodos")
        val novoComodoId = database.push().key // Gera um ID único
        val comodo = Comodo(nomeComodo = nomeComodo, descricao = descricao)

        novoComodoId?.let {
            database.child(it).setValue(comodo).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Cômodo adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registrarComodoFragment_to_homeFragment)
                } else {
                    Toast.makeText(requireContext(), "Erro ao adicionar cômodo!", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Erro na conexão com o servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }
}