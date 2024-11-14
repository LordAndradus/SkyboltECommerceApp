package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import edu.utsa.cs3443.skyboltecommerceapp.Data.Product

abstract class ParentRecyclerViewAdapter<T: ViewBinding>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> T,
): RecyclerView.Adapter<ParentRecyclerViewAdapter<T>.ViewHolder>() {

    inner class ViewHolder(val binding: T): RecyclerView.ViewHolder(binding.root)
    {
        fun getImageView(): View
        {
            return itemView
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>()
    {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean
        {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean
        {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    abstract fun bindFunction(binding: T, vh: ParentRecyclerViewAdapter<T>.ViewHolder, product: Product)

    lateinit var view: T
    lateinit var vh: ParentRecyclerViewAdapter<T>.ViewHolder

    fun getGenericType(): T
    {
        return view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        view = inflateBinding(LayoutInflater.from((parent.context)), parent, false)
        vh = ViewHolder(view)
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = differ.currentList[position]
        bindFunction(view, vh, item)
    }

    override fun getItemCount(): Int
    {
        return differ.currentList.size
    }
}