package my.com.teddyFood.staffManagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.teddyFood.R


class StaffAdapter(val fn: (ViewHolder, Staff) -> Unit = { _, _ -> })
    :ListAdapter<Staff, StaffAdapter.ViewHolder>(DiffCallback) {


    companion object DiffCallback : DiffUtil.ItemCallback<Staff>() {
        override fun areItemsTheSame(a: Staff, b: Staff)    = a.id == b.id
        override fun areContentsTheSame(a: Staff, b: Staff) = a == b
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtID: TextView=view.findViewById((R.id.showID))
        val txtName    : TextView = view.findViewById(R.id.showName)
        val editBtn:ImageButton=view.findViewById(R.id.editbtn)
        val deleteBtn:ImageButton=view.findViewById(R.id.deletebtn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_listing_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val staff = getItem(position)
        holder.txtID.text = staff.id
        holder.txtName.text = staff.name


        fn(holder,staff)
    }




}