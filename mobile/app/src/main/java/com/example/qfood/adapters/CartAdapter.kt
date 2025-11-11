package com.example.qfood.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qfood.R
import com.example.qfood.activities.MainActivity
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.item.CartFood

class CartAdapter(val lists: List<CartFood>, val identify: Int, val context: Context?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CartItemViewHolder(
        itemView: View,
        deleteListener: RecyclerViewInterface,
        minusListener: RecyclerViewInterface,
        plusListener: RecyclerViewInterface
    ) : RecyclerView.ViewHolder(itemView) {
        val cartItemImage: ImageView
        val cartItemName: TextView
        val cartItemDesc: TextView
        val cartItemDelete: TextView
        val cartItemPrice: TextView
        val cartItemDiscount: TextView
        val cartItemMinus: TextView
        val cartItemQty: TextView
        val cartItemPlus: TextView

        init {
            // cart info
            cartItemImage = itemView.findViewById<ImageView>(R.id.cartItemImage)
            cartItemName = itemView.findViewById<TextView>(R.id.cartItemName)
            cartItemDesc = itemView.findViewById<TextView>(R.id.cartItemDesc)
            cartItemDelete = itemView.findViewById<TextView>(R.id.cartItemDelete)
            cartItemPrice = itemView.findViewById<TextView>(R.id.cartItemPrice)
            cartItemDiscount = itemView.findViewById<TextView>(R.id.cartItemDiscount)
            cartItemMinus = itemView.findViewById<TextView>(R.id.cartItemMinus)
            cartItemQty = itemView.findViewById<TextView>(R.id.cartItemQty)
            cartItemPlus = itemView.findViewById<TextView>(R.id.cartItemPlus)

            // onclick delete
            cartItemDelete.setOnClickListener {
                deleteListener.onItemClick(adapterPosition)
            }

            // onclick minus
            cartItemMinus.setOnClickListener {
                minusListener.onItemClick(adapterPosition)
            }

            // onclick plus
            cartItemPlus.setOnClickListener {
                plusListener.onItemClick(adapterPosition)
            }

        }
    }

    lateinit var deleteListener: RecyclerViewInterface
    lateinit var minusListener: RecyclerViewInterface
    lateinit var plusListener: RecyclerViewInterface

    fun setOnDeleteClickListener(listener: RecyclerViewInterface) {
        deleteListener = listener
    }

    fun setOnMinusClickListener(listener: RecyclerViewInterface) {
        minusListener = listener
    }

    fun setOnPlusClickListener(listener: RecyclerViewInterface) {
        plusListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return identify
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> return CartItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false),
                deleteListener,
                minusListener,
                plusListener
            )
            else -> return CartItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false),
                deleteListener,
                minusListener,
                plusListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartItemViewHolder) {
            holder.apply {
                // set info
                cartItemImage.setImageDrawable((context as MainActivity).getImage(lists[position].food_src))
                cartItemName.text = lists[position].food_name
                cartItemDesc.text = lists[position].food_desc
                cartItemQty.text = lists[position].item_qty.toString()
                cartItemPrice.text = "$" + lists[position].calcTotal().toString()
                if (lists[position].food_discount!!.toDouble() != 0.00) {
                    cartItemDiscount.visibility = View.VISIBLE
                    cartItemDiscount.text = "$" + lists[position].calcPrice().toString()
                    cartItemDiscount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
                } else {
                    cartItemDiscount.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }
}