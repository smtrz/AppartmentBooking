package com.tahir.airmeetask.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tahir.airmeetask.R
import com.tahir.airmeetask.interfaces.RVClickCallback
import com.tahir.airmeetask.models.Appartments
import kotlinx.android.synthetic.main.appartment_row.view.*


open class AppartmentsAdapter(
    var context: Context,
    var notes: List<Appartments>?,
    var rv: RVClickCallback

)

    : RecyclerView.Adapter<AppartmentsAdapter.AppartmentViewHolder>() {
    var listener: RVClickCallback? = null

    init {
        listener = rv
    }

    fun loadItems(newItems: List<Appartments>, rv: RVClickCallback) {
        notes = newItems
        listener = rv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppartmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.appartment_row, parent, false)

        return AppartmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppartmentViewHolder, position: Int) {

        // Binding all the data with the views.
        try {
            holder.title?.text = notes?.get(position)?.name
            holder.app_desc?.text = notes?.get(position)?.bedrooms.toString() + " bedrooms"
            holder.app_distance?.text = notes?.get(position)?.distance.toString() + " KMs away"
            if (notes?.get(position)!!.isBooked) {
                holder.app_booked?.visibility = View.VISIBLE

            } else {
                holder.app_booked?.visibility = View.INVISIBLE


            }


        } catch (e: Exception) {
        }

    }

    override fun getItemCount(): Int {
        var count = 0

        if (notes != null && notes!!.count() != 0) {
            count = notes!!.size
        }
        return count

    }

    inner class AppartmentViewHolder
        (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {


        init {

            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        internal var title: TextView? = itemView.title
        internal var app_desc: TextView? = itemView.description
        internal var app_booked: TextView? = itemView.app_booked
        internal var app_distance: TextView? = itemView.app_distance
        internal var imageUrl: ImageView? = itemView.notes_img
        override fun onClick(p0: View?) {
            listener?.onItemClick(adapterPosition)

        }

        override fun onLongClick(p0: View?): Boolean {
            return true

        }


    }


}




