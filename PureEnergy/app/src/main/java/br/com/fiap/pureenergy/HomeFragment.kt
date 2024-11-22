package br.com.fiap.pureenergy

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.fiap.pureenergy.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    // Lista para armazenar os pares (ID do cômodo, Nome do cômodo)
    private val comodos = mutableListOf<Pair<String, String>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val userId = auth.currentUser?.uid

        // Carregar o nome do usuário
        userId?.let {
            database.getReference("Users").child(it).get()
                .addOnSuccessListener { snapshot ->
                    val nomeUsuario = snapshot.child("nomeUsuario").getValue(String::class.java)
                    binding.textUserName.text = nomeUsuario ?: "Usuário"
                }
                .addOnFailureListener {
                    binding.textUserName.text = "Erro ao buscar nome do usuário"
                }
        }

        // Configurar o RecyclerView
        binding.recyclerViewCards.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewCards.addItemDecoration(
            GridSpacingItemDecoration(spanCount = 2, spacing = 8, includeEdge = true)
        )

        // Buscar dados no Firebase
        fetchComodos()

        // Configurar o TabLayout para alternar os itens
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Cômodos"))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Configurar o adaptador com os nomes dos cômodos
                binding.recyclerViewCards.adapter = CardAdapter(comodos.map { it.second }.toMutableList(),
                    onItemClicked = { selectedItem ->
                        val comodoId = comodos.find { it.second == selectedItem }?.first
                        if (comodoId != null) {
                            val bundle = Bundle().apply {
                                putString("comodoId", comodoId)
                            }
                            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
                        } else {
                            Log.e("HomeFragment", "ComodoId não encontrado para o item selecionado: $selectedItem")
                        }
                    },
                    onDeleteItem = { deletedItem ->
                        // Exclua o item do Firebase
                        val comodoId = comodos.find { it.second == deletedItem }?.first
                        if (comodoId != null) {
                            val database = FirebaseDatabase.getInstance()
                            val comodoRef = database.getReference("Comodos").child(comodoId)
                            comodoRef.removeValue()
                                .addOnSuccessListener {
                                    Log.d("HomeFragment", "Cômodo excluído com sucesso: $deletedItem")
                                }
                                .addOnFailureListener { error ->
                                    Log.e("HomeFragment", "Erro ao excluir o cômodo: ${error.message}")
                                }
                        }
                    }
                )

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


        // Configurar Bottom Navigation
        binding.bottomNavigationHome.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> true // Permanecer na Home
                R.id.nav_register_room -> {
                    findNavController().navigate(R.id.action_homeFragment_to_registrarComodoFragment)
                    true
                }
                R.id.nav_register_device -> {
                    findNavController().navigate(R.id.action_homeFragment_to_registrarAparelhoFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchComodos() {
        val comodosRef = database.getReference("Comodos")

        comodosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Verifique se o fragmento ainda está anexado e o binding não é nulo
                if (!isAdded || _binding == null) {
                    Log.e("HomeFragment", "Fragment não está anexado ou binding é nulo.")
                    return
                }

                comodos.clear()
                for (child in snapshot.children) {
                    val comodoId = child.key
                    val nomeComodo = child.child("nomeComodo").getValue(String::class.java)

                    if (comodoId != null && nomeComodo != null) {
                        comodos.add(Pair(comodoId, nomeComodo))
                    }
                }

                binding.recyclerViewCards.adapter = CardAdapter(
                    comodos.map { it.second }.toMutableList(), // Converte para MutableList<String>
                    onItemClicked = { selectedItem ->
                        val comodoId = comodos.find { it.second == selectedItem }?.first
                        if (comodoId != null) {
                            val bundle = Bundle().apply {
                                putString("comodoId", comodoId)
                            }
                            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
                        } else {
                            Log.e("HomeFragment", "ComodoId não encontrado para o item selecionado: $selectedItem")
                        }
                    },
                    onDeleteItem = { deletedItem ->
                        val comodoId = comodos.find { it.second == deletedItem }?.first
                        if (comodoId != null) {
                            val database = FirebaseDatabase.getInstance()
                            val comodoRef = database.getReference("Comodos").child(comodoId)
                            comodoRef.removeValue()
                                .addOnSuccessListener {
                                    Log.d("HomeFragment", "Cômodo excluído com sucesso: $deletedItem")
                                }
                                .addOnFailureListener { error ->
                                    Log.e("HomeFragment", "Erro ao excluir o cômodo: ${error.message}")
                                }
                        }
                    }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeFragment", "Erro ao carregar cômodos: ${error.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
