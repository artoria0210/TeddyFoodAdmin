package volunteerManagement

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.com.teddyFood.R
import my.com.teddyFood.databinding.FragmentVolunteerListingBinding


class VolunteerListingFragment : Fragment() {
    private lateinit var binding: FragmentVolunteerListingBinding
    private val nav by lazy { findNavController() }
    private val vm: VolunteerViewModel by activityViewModels()

    private lateinit var adapter: VolunteerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentVolunteerListingBinding.inflate(inflater,container, false)
        adapter = VolunteerAdapter()
        {
                holder, s ->  holder.root.setOnClickListener {nav.navigate(R.id.volunteerDetailFragment,
            bundleOf("id" to s.id))}
            holder.deleteBtn.setOnClickListener{ delete(s.id)}
        }
        load()
        binding.searchvolunteer.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                // Search by [name] -> vm.search(...)
                vm.search(name)
                return true
            }
        })
        binding.btnVolunName.setOnClickListener { sort("name") }
        binding.btnVolunEmail.setOnClickListener { sort("email") }

        binding.srv.adapter = adapter
        binding.srv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        return binding.root
    }

    private fun load() {
        // TODO: Load data
        vm.getAll().observe(viewLifecycleOwner){ volunteers->
            adapter.submitList(volunteers)
        }
    }

    private fun delete(id:String){
        Firebase.firestore
            .collection("volunteers")
            .document(id)
            .delete()
    }

    private fun sort(field: String) {
        // Sort by [field] -> vm.sort(...)
        val reverse = vm.sort(field)

        // Remove icon -> all buttons
        binding.btnVolunName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnVolunEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        //Set icon -> specific button
        val res = if(reverse) R.drawable.ic_down else R.drawable.ic_up

        when (field) {
            "name" -> binding.btnVolunName.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "email" -> binding.btnVolunEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
        }
    }
}