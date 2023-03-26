package com.lineup.chatgptgame.recyclerview

import android.graphics.Color
import android.graphics.Color.YELLOW
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lineup.chatgptgame.R

// In your Kotlin code:
class MyAdapter(private val myDataset: ArrayList<String>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        //Way 1
//        val roundedCornerShape = RoundRectShape(
//            floatArrayOf(15f, 15f, 15f, 15f, 15f, 15f, 15f, 15f),  // rounded corners
//            null,
//            null
//        )
//        val shapeDrawable = ShapeDrawable(roundedCornerShape)
//        val view = View(parent.context)
//        view.background = shapeDrawable

        //Way 2
        val view = LayoutInflater.from(parent.context).inflate(R.layout.baritemview, parent, false)
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(myDataset[position]=="yellow"){
            holder.view.backgroundTintList=holder.view.context.resources.getColorStateList(R.color.Yellow)
        }
        else{
            holder.view.backgroundTintList=holder.view.context.resources.getColorStateList(R.color.Red)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    fun getData():ArrayList<String>{
        return myDataset
    }
}