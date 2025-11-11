package com.example.qfood.interfaces

import android.widget.ImageView

interface RecyclerViewInterface {
    fun onItemClick(position: Int);
}

interface RecyclerViewInterfaceAnimation {
    fun onItemClick(position: Int, imageView: ImageView);
}