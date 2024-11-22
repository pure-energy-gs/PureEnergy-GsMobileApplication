package br.com.fiap.pureenergy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ComodoAdapter(private val items: List<Comodo>) : RecyclerView.Adapter<ComodoAdapter.ComodoViewHolder>() {

    // Classe ViewHolder que associa os elementos do layout
    inner class ComodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeComodo: TextView = itemView.findViewById(R.id.textNomeComodo)
        val descricaoComodo: TextView = itemView.findViewById(R.id.inputUtilidadeComodo) // Opcional, caso queira exibir descrição
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ComodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComodoViewHolder, position: Int) {
        val comodo = items[position]
        holder.nomeComodo.text = comodo.nomeComodo
        holder.descricaoComodo.text = comodo.descricao // Exiba a descrição, se necessário

        // Clique no card, se quiser navegar para outra tela
        holder.itemView.setOnClickListener {
            // Adicione a lógica para navegar ou executar ações específicas
        }
    }

    override fun getItemCount(): Int = items.size
}