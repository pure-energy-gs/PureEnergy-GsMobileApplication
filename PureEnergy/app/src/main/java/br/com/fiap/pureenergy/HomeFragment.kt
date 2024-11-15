package br.com.fiap.pureenergy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import br.com.fiap.pureenergy.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
class HomeFragment: Fragment(R.layout.fragment_home)  {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().getReference("Users")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        userId?.let {
            database.child(it).get().addOnSuccessListener { snapshot ->
                val nomeUsuario = snapshot.child("nomeUsuario").getValue(String::class.java)
                binding.textUserName.text = "$nomeUsuario"
            }.addOnFailureListener {
                binding.textUserName.text = "Erro ao buscar nome do usuário"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}