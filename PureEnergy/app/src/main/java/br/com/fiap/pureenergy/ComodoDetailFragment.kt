package br.com.fiap.pureenergy

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.pureenergy.databinding.FragmentComodoDetailBinding
import com.google.firebase.database.*

class ComodoDetailFragment : Fragment(R.layout.fragment_comodo_detail) {

    private var _binding: FragmentComodoDetailBinding? = null
    private val binding get() = _binding!!

    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private lateinit var comodoId: String
    private val aparelhos = mutableListOf<Aparelho>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentComodoDetailBinding.bind(view)

        // Obter o ID do cômodo recebido como argumento
        comodoId = arguments?.getString("comodoId") ?: ""
        Log.d("ComodoDetailFragment", "ComodoId recebido: $comodoId")

        setupRecyclerView()
        fetchComodoDetails()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewAparelhos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAparelhos.adapter = AparelhoAdapter(aparelhos)
    }

    private fun fetchComodoDetails() {
        if (comodoId.isEmpty()) {
            Log.e("ComodoDetailFragment", "ComodoId está vazio!")
            return
        }

        val comodoRef = firebaseDatabase.getReference("Comodos").child(comodoId)

        comodoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nomeComodo = snapshot.child("nomeComodo").getValue(String::class.java) ?: "Sem Nome"
                val aparelhosSnapshot = snapshot.child("aparelhos")

                // Atualiza o título do cômodo
                binding.textComodoTitle.text = nomeComodo

                // Limpa a lista atual e adiciona os aparelhos encontrados
                aparelhos.clear()
                for (child in aparelhosSnapshot.children) {
                    val aparelho = child.getValue(Aparelho::class.java)
                    aparelho?.let { aparelhos.add(it) }
                }

                binding.recyclerViewAparelhos.adapter?.notifyDataSetChanged()
                Log.d("ComodoDetailFragment", "Aparelhos carregados: $aparelhos")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ComodoDetailFragment", "Erro ao carregar os dados do cômodo: ${error.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
