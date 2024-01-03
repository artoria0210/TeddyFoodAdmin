package my.com.teddyFood.Events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import my.com.teddyFood.R
import my.com.teddyFood.databinding.FragmentEventsManagementBinding

class EventsManagementFragment : Fragment() {

    private lateinit var binding: FragmentEventsManagementBinding
    private val nav by lazy { findNavController() }
    private val vm: EventsViewModel by activityViewModels()

    private lateinit var adapter: EventsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentEventsManagementBinding.inflate(inflater, container, false)

        binding.btnInsert.setOnClickListener { nav.navigate(R.id.eventsInsertFragment) }
        binding.btnDeleteAll.setOnClickListener { deleteAll() }

        //Default search, filter and sort
        vm.search("")
        vm.filter("")
        sort("id")

        adapter = EventsAdapter() { holder, event ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.eventsUpdateFragment, bundleOf("id" to event.id))
            }
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.txtCount.text = "${list.size} event(s)"
        }

        binding.sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(title: String) = true
            override fun onQueryTextChange(title: String): Boolean {
                // Search by [title] -> vm.search(...)
                vm.search(title)
                return true
            }
        })

        binding.btnId.setOnClickListener { sort("id") }
        binding.btnTitle.setOnClickListener { sort("title") }

        return binding.root
    }

    private fun deleteAll() {
        vm.deleteAll()
    }

    private fun delete(id: String) {
        vm.delete(id)
    }

    private fun sort(field: String) {
        // Sort by [field] -> vm.sort(...)
        val reverse = vm.sort(field)

        // Remove icon -> all buttons
        binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        //Set icon -> specific button
        val res = if(reverse) R.drawable.ic_down else R.drawable.ic_up

        when (field) {
            "id" -> binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "title" -> binding.btnTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
        }
    }
}