package my.com.teddyFood.Events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.teddyFood.R
import my.com.teddyFood.toBitmap

class EventsAdapter(
    val fn: (ViewHolder, Event) -> Unit = {_, _ -> }
) : ListAdapter<Event, EventsAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(a: Event, b: Event) = a.id == b.id
        override fun areContentsTheSame(a: Event, b: Event) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val root = view
        val imgPhoto : ImageView = view.findViewById(R.id.imgPhoto)
        val txtId    : TextView = view.findViewById(R.id.txtId)
        val txtTitle  : TextView = view.findViewById(R.id.txtTitle)
        val txtDesc   : TextView = view.findViewById(R.id.txtDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)

        holder.txtId.text   = event.id
        holder.txtTitle.text = event.title
        holder.txtDesc.text  = event.description

        // TODO: Photo (blob to bitmap)
        holder.imgPhoto.setImageBitmap(event.photo.toBitmap())

        fn(holder, event)
    }
}