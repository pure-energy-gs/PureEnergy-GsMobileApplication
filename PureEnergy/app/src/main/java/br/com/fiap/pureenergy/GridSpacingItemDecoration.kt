package br.com.fiap.pureenergy

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = spacing - column * spacing / spanCount
        outRect.right = (column + 1) * spacing / spanCount

        // Ajuste do espaçamento superior
        if (position < spanCount) {
            outRect.top = spacing / 2 // Reduzir o espaçamento inicial
        }

        // Ajuste do espaçamento inferior
        outRect.bottom = spacing / 2 // Reduzir o espaçamento entre os cards
    }
}