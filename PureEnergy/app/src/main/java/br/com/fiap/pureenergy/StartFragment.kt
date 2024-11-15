package br.com.fiap.pureenergy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_entrar_login).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        view.findViewById<Button>(R.id.btn_tela_cadastrar).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_cadastroFragment)
        }
    }
}
