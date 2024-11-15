package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.utsa.cs3443.skyboltecommerceapp.Data.Order
import edu.utsa.cs3443.skyboltecommerceapp.Data.OrderStatus
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.databinding.RvOrderItemBinding

class AllOrdersAdapter: RecyclerView.Adapter<AllOrdersAdapter.ViewHolder>()
{
    inner class ViewHolder(val binding: RvOrderItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(order: Order)
        {
            binding.apply {
                tvOrderId.text = order.orderID.toString()
                tvOrderDate.text = order.date
                val resources = itemView.resources

                val colorDrawable = when (order.orderStatus)
                {
                    OrderStatus.PLACED -> {
                        ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    }

                    OrderStatus.CONFIRMED -> {
                        ColorDrawable(resources.getColor(R.color.purple_200))
                    }

                    OrderStatus.SHIPPED -> {
                        ColorDrawable(resources.getColor(R.color.teal_700))
                    }

                    OrderStatus.DELIVERED -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }

                    OrderStatus.CANCELLED -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }

                    OrderStatus.RETURNED -> {
                        ColorDrawable(resources.getColor(R.color.g_light_red))
                    }
                }

                imageOrderState.setImageDrawable(colorDrawable)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Order>()
    {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean
        {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean
        {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            RvOrderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val order = differ.currentList[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(order)
        }
    }

    override fun getItemCount(): Int
    {
        return differ.currentList.size
    }

    var onItemClick:((Order) -> Unit)?=null
}