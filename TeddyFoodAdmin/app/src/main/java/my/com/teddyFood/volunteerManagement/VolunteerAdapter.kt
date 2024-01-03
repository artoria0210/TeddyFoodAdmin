package volunteerManagement


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.teddyFood.R


class VolunteerAdapter (val fn: (ViewHolder,Volunteer) -> Unit = { _, _ -> })
    :ListAdapter<Volunteer,VolunteerAdapter.ViewHolder>(DiffCallback)
{
    companion object DiffCallback : DiffUtil.ItemCallback<Volunteer>() {
        override fun areItemsTheSame(a: Volunteer, b: Volunteer)    = a.id == b.id
        override fun areContentsTheSame(a: Volunteer, b: Volunteer) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtName   : TextView =view.findViewById((R.id.volunName))
        val txtEmail  : TextView = view.findViewById(R.id.volunEmail)

        //val btnDelete: Button = view.findViewById(R.id.btnDeleteAll)
        val deleteBtn   : ImageButton =view.findViewById(R.id.btnDeleteVolun)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_volunteer_listing, parent, false)
        return VolunteerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: VolunteerAdapter.ViewHolder, position: Int){
        val volunteer = getItem(position)
        holder.txtName.text = volunteer.name
        holder.txtEmail.text = volunteer.email


        fn(holder,volunteer)
    }
}