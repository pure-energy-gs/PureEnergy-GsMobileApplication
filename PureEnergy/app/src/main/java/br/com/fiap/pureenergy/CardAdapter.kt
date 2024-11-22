package br.com.fiap.pureenergy

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

class CardAdapter(
    private val items: MutableList<String>,
    private val onItemClicked: (String) -> Unit,
    private val onDeleteItem: (String) -> Unit
) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.textNomeComodo)
        val imageOpcoes: ImageView = itemView.findViewById(R.id.imageOpcoes) // Certifique-se do ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textTitle.text = item

        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }

        holder.imageOpcoes.setOnClickListener {
            val popupMenu = PopupMenu(holder.itemView.context, holder.imageOpcoes)
            popupMenu.menu.add("Excluir")
            popupMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.title == "Excluir") {
                    onDeleteItem(item)
                }
                true
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int = items.size

    fun removeItem(item: String) {
        val position = items.indexOf(item)
        if (position >= 0) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
