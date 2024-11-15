package br.com.fiap.pureenergy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.fiap.pureenergy.databinding.FragmentCadastroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CadastroFragment : Fragment(R.layout.fragment_cadastro) {

    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.imageView2.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.idButtonCadastrar.setOnClickListener {
            val nomeUsuario = binding.idNomeUsuario.text.toString()
            val nomeCompleto = binding.idNomeCompleto.text.toString()
            val email = binding.idEmailCadastro.text.toString()
            val senha = binding.editTextTextPassword.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty() && nomeUsuario.isNotEmpty() && nomeCompleto.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val database = FirebaseDatabase.getInstance().getReference("Users")
                            userId?.let {
                                val userMap = mapOf(
                                    "nomeUsuario" to nomeUsuario,
                                    "nomeCompleto" to nomeCompleto
                                )
                                database.child(it).setValue(userMap)
                                    .addOnCompleteListener {
                                        Toast.makeText(requireContext(), "Cadastro realizado!", Toast.LENGTH_SHORT).show()
                                        findNavController().navigate(R.id.action_cadastroFragment_to_loginFragment)
                                    }
                            }
                        } else {
                            Toast.makeText(requireContext(), "Falha no cadastro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textLogin.setOnClickListener {
            findNavController().navigate(R.id.action_cadastroFragment_to_loginFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}