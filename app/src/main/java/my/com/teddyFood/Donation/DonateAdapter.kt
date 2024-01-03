package my.com.teddyFood.Donation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.teddyFood.R

class DonateAdapter (
    val fn: (ViewHolder, Donate) -> Unit = { _, _ -> }
) : ListAdapter<Donate, DonateAdapter.ViewHolder>(DiffCallback)  {

    companion object DiffCallback : DiffUtil.ItemCallback<Donate>() {
        override fun areItemsTheSame(a: Donate, b: Donate)    = a.id == b.id
        override fun areContentsTheSame(a: Donate, b: Donate) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtName    : TextView = view.findViewById(R.id.txtName)
        val txtType  : TextView = view.findViewById(R.id.txtType)
        val txtQuantity   : TextView = view.findViewById(R.id.txtQuantity)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_donate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donation = getItem(position)

        holder.txtName.text = donation.name
        holder.txtType.text = donation.type
        holder.txtQuantity.text  = donation.quantity.toString()

        fn(holder, donation)
    }
}