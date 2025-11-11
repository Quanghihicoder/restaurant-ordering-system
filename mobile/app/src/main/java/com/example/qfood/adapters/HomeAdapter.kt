package com.example.qfood.adapters

import android.content.Context
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
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.item.Food

class HomeAdapter(val categories: List<Food>, val identify: Int, val context: Context?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HomeCategoryViewHolder(itemView: View, listener: RecyclerViewInterface) :
        RecyclerView.ViewHolder(itemView) {
        val categoryItem: CardView
        val categoryImage: ImageView
        val categoryText: TextView

        init {
            categoryItem = itemView.findViewById(R.id.categoryItem)
            categoryImage = itemView.findViewById(R.id.categoryImage)
            categoryText = itemView.findViewById(R.id.categoryText)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    inner class HomeBestViewHolder(itemView: View, listener: RecyclerViewInterface) :
        RecyclerView.ViewHolder(itemView) {
        val bestItem: CardView
        val bestImage: ImageView
        val bestText: TextView

        val bestRate: TextView
        val bestVote: TextView
        val bestTag1: CardView
        val bestTag2: CardView
        val bestTag3: CardView
        val bestTag1Text: TextView
        val bestTag2Text: TextView
        val bestTag3Text: TextView

        init {
            bestItem = itemView.findViewById(R.id.bestItem)
            bestImage = itemView.findViewById(R.id.bestImage)
            bestText = itemView.findViewById(R.id.bestText)

            bestRate = itemView.findViewById(R.id.bestRate)
            bestVote = itemView.findViewById(R.id.bestVote)

            bestTag1 = itemView.findViewById(R.id.bestTag1)
            bestTag2 = itemView.findViewById(R.id.bestTag2)
            bestTag3 = itemView.findViewById(R.id.bestTag3)
            bestTag1Text = itemView.findViewById(R.id.bestTag1Text)
            bestTag2Text = itemView.findViewById(R.id.bestTag2Text)
            bestTag3Text = itemView.findViewById(R.id.bestTag3Text)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    inner class HomeDisViewHolder(itemView: View, listener: RecyclerViewInterface) :
        RecyclerView.ViewHolder(itemView) {
        val disImage: ImageView
        val disAmount: TextView

        init {
            disImage = itemView.findViewById(R.id.disImage)
            disAmount = itemView.findViewById(R.id.disAmount)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    lateinit var mListener: RecyclerViewInterface

    fun setOnItemClickListener(listener: RecyclerViewInterface) {
        mListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return identify
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> return HomeCategoryViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_home_category, parent, false), mListener
            )
            2 -> return HomeBestViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_home_best, parent, false),
                mListener
            )
            3 -> return HomeDisViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_home_dis, parent, false),
                mListener
            )
            else -> return HomeCategoryViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_home_category, parent, false), mListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeCategoryViewHolder) {
            holder.apply {
                categoryImage.setImageDrawable((context as MainActivity).getImage(categories[position].food_src))
                categoryText.text = categories[position].food_name
                when (position) {
                    0 -> categoryItem.setCardBackgroundColor(itemView.resources.getColor(R.color.greenPakistan))
                    1 -> categoryItem.setCardBackgroundColor(itemView.resources.getColor(R.color.greenDarkMoss))
                    2 -> categoryItem.setCardBackgroundColor(itemView.resources.getColor(R.color.greenHooker))
                    3 -> categoryItem.setCardBackgroundColor(itemView.resources.getColor(R.color.yellowMain))
                    4 -> categoryItem.setCardBackgroundColor(itemView.resources.getColor(R.color.yellowEarth))
                    5 -> categoryItem.setCardBackgroundColor(itemView.resources.getColor(R.color.orangeTiger))
                }
            }
        }

        if (holder is HomeBestViewHolder) {
            val animation: Animation =
                AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)

            holder.apply {
                bestImage.setImageDrawable((context as MainActivity).getImage(categories[position].food_src))
                bestText.text = categories[position].food_name
                bestRate.text = categories[position].food_star
                bestVote.text = "(" + categories[position].food_vote + "+)"

                bestTag1.visibility = View.VISIBLE
                bestTag1Text.text = categories[position].food_category

                if (categories[position].food_status!!.contains("seasonal dishes")) {
                    bestTag2.visibility = View.VISIBLE
                    bestTag2Text.text = "seasonal"
                } else {
                    bestTag2.visibility = View.GONE
                }

                if (categories[position].food_status!!.contains("new dishes")) {
                    bestTag3.visibility = View.VISIBLE
                    bestTag3Text.text = "new"
                } else {
                    bestTag3.visibility = View.GONE
                }
                itemView.startAnimation(animation)
            }
        }

        if (holder is HomeDisViewHolder) {
            val animation: Animation =
                AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
            holder.apply {
                disImage.setImageDrawable((context as MainActivity).getImage(categories[position].food_src))
                disAmount.text =
                    (categories[position].food_discount!!.toDouble() / categories[position].food_price!!.toDouble() * 100).toInt()
                        .toString() + "%"

                itemView.startAnimation(animation)
            }
        }

    }

    override fun getItemCount(): Int {
        return categories.size
    }

}