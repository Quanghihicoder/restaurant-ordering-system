package com.example.qfood.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.qfood.R
import com.example.qfood.activities.MainActivity
import com.example.qfood.fragments.MenuFragment
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.interfaces.RecyclerViewInterfaceAnimation
import com.example.qfood.item.Food

class MenuAdapter(
    val lists: List<Food>,
    val identify: Int,
    val context: Context?,
    val fragment: MenuFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MenuTypeViewHolder(itemView: View, listener: RecyclerViewInterface) :
        RecyclerView.ViewHolder(itemView) {
        val typeItem: CardView
        val typeImage: ImageView

        init {
            typeItem = itemView.findViewById(R.id.typeItem)
            typeImage = itemView.findViewById(R.id.typeImage)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    inner class MenuFoodViewHolder(itemView: View, listener: RecyclerViewInterfaceAnimation) :
        RecyclerView.ViewHolder(itemView) {
        val foodItem: CardView
        val foodImage: ImageView
        val foodName: TextView

        val foodVote: TextView
        val foodPrice: TextView
        val foodDis: TextView


        init {
            foodItem = itemView.findViewById(R.id.menuFoodItem)
            foodImage = itemView.findViewById(R.id.menuFoodImage)
            foodName = itemView.findViewById(R.id.menuFoodName)
            foodVote = itemView.findViewById(R.id.menuFoodVote)
            foodPrice = itemView.findViewById(R.id.menuFoodPrice)
            foodDis = itemView.findViewById(R.id.menuFoodDiscount)


            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, foodImage)
            }
        }
    }

    lateinit var cateListener: RecyclerViewInterface
    lateinit var foodListener: RecyclerViewInterfaceAnimation

    fun setOnCateClickListener(listener: RecyclerViewInterface) {
        cateListener = listener
    }

    fun setOnFoodClickListener(listener: RecyclerViewInterfaceAnimation) {
        foodListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return identify
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> return MenuTypeViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_menu_type, parent, false),
                cateListener
            )
            2 -> return MenuFoodViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_menu_food, parent, false),
                foodListener
            )
            else -> return MenuTypeViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_menu_type, parent, false),
                cateListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MenuTypeViewHolder) {
            holder.apply {
                typeImage.setImageDrawable((context as MainActivity).getImage(lists[position].food_src))
                if (position == fragment.foodCate) {
                    typeItem.setCardBackgroundColor(context.resources.getColor(R.color.greenMain))
                } else {
                    typeItem.setCardBackgroundColor(Color.WHITE)
                }
            }

        }

        if (holder is MenuFoodViewHolder) {
            val animation: Animation =
                AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
            holder.apply {
                foodImage.setImageDrawable((context as MainActivity).getImage(lists[position].food_src))
                foodName.text = lists[position].food_name
                foodVote.text = "(" + lists[position].food_vote + "+)"
                foodPrice.text = "$" + calcPrice(
                    lists[position].food_price!!.toDouble(),
                    lists[position].food_discount!!.toDouble()
                ).toString()
                if (lists[position].food_discount!!.toDouble() != 0.00) {
                    foodDis.visibility = View.VISIBLE
                    foodDis.text = "$" + lists[position].food_price
                    foodDis.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
                } else {
                    foodDis.visibility = View.GONE
                }
                itemView.startAnimation(animation)
            }
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    fun calcPrice(price: Double, dis: Double): Double {
        return price - dis
    }
}