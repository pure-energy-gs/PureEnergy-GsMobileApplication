package br.com.fiap.pureenergy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AparelhoAdapter(private val items: List<Aparelho>) :
    RecyclerView.Adapter<AparelhoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeAparelho: TextView = itemView.findViewById(R.id.textNomeAparelho)
        val potencia: TextView = itemView.findViewById(R.id.textPotencia)
        val horasUso: TextView = itemView.findViewById(R.id.textHorasUso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aparelho, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aparelho = items[position]
        holder.nomeAparelho.text = aparelho.nomeAparelho
        holder.potencia.text = "Potência: ${aparelho.potencia}W"
        holder.horasUso.text = "Horas de uso: ${aparelho.horasUso}h/dia"
    }

    override fun getItemCount(): Int = items.size
}