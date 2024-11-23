package br.com.fiap.pureenergy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import br.com.fiap.pureenergy.databinding.FragmentRegistrarAparelhoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegistrarAparelhoFragment : Fragment(R.layout.fragment_registrar_aparelho) {

    private var _binding: FragmentRegistrarAparelhoBinding? = null
    private val binding get() = _binding!!
    private val comodos = mutableListOf<Pair<String, String>>() // Armazenar ID e nome dos cômodos

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegistrarAparelhoBinding.bind(view)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val btnBack = view.findViewById<View>(R.id.btnBack)

        // Configuração do botão voltar
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Carregar os cômodos para o spinner
        fetchComodos()

        // Configurar botão para adicionar aparelho
        binding.btnAdicionarAparelho.setOnClickListener {
            val nomeAparelho = binding.inputNomeAparelho.text.toString().trim()
            val potencia = binding.inputPotenciaAparelho.text.toString().trim()
            val horasUso = binding.inputHorasUsoDia.text.toString().trim()
            val comodoSelecionado = binding.inputComodo.selectedItemPosition

            // Verificar campos obrigatórios
            if (nomeAparelho.isEmpty() || potencia.isEmpty() || horasUso.isEmpty() || comodoSelecionado !in comodos.indices) {
                Toast.makeText(requireContext(), "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obter o ID do cômodo selecionado
            val comodoId = comodos[comodoSelecionado].first

            // Criar objeto do aparelho
            val aparelho = Aparelho(
                nomeAparelho = nomeAparelho,
                potencia = potencia.toIntOrNull() ?: 0,
                horasUso = horasUso.toIntOrNull() ?: 0,
                descricao = binding.inputDescricaoAparelho.text.toString().trim()
            )

            // Verificar usuário autenticado
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(requireContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Salvar no Firebase
            val database = FirebaseDatabase.getInstance()
            val comodoRef = database.getReference("Comodos").child(comodoId).child("aparelhos")
            comodoRef.push().setValue(aparelho)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Aparelho adicionado com sucesso", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { error ->
                    Log.e("RegistrarAparelhoFragment", "Erro ao salvar aparelho: ${error.message}")
                    Toast.makeText(requireContext(), "Erro ao adicionar aparelho: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Configuração da barra de navegação
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.action_registrarAparelhoFragment_to_homeFragment)
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
                    findNavController().navigate(R.id.action_registrarAparelhoFragment_to_registrarComodoFragment)
                    true
                }
                R.id.nav_register_device -> {
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchComodos() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Comodos")
        databaseRef.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                comodos.clear()
                for (child in snapshot.children) {
                    val comodoId = child.key ?: continue
                    val nomeComodo = child.child("nomeComodo").getValue(String::class.java) ?: "Sem Nome"
                    comodos.add(Pair(comodoId, nomeComodo))
                }

                // Atualizar o spinner com os nomes dos cômodos
                if (_binding != null) {
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        comodos.map { it.second }
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.inputComodo.adapter = adapter
                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Log.e("RegistrarAparelhoFragment", "Erro ao carregar cômodos: ${error.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

