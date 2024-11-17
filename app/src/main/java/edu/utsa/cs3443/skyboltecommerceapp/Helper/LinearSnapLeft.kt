package edu.utsa.cs3443.skyboltecommerceapp.Helper

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class LinearSnapLeft: LinearSnapHelper()
{
    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray {
        val distance = IntArray(2)
        val childStart = layoutManager.getDecoratedLeft(targetView)
        distance[0] = childStart
        distance[1] = 0
        return distance
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        return if (layoutManager is RecyclerView.SmoothScroller.ScrollVectorProvider)
        {
            super.findSnapView(layoutManager)
        }
        else
        {
            null
        }
    }
}