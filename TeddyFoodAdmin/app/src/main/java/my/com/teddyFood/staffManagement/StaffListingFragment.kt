package my.com.teddyFood.staffManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.com.teddyFood.R
import my.com.teddyFood.databinding.FragmentStaffListingBinding


class StaffListingFragment : Fragment() {

    private lateinit var binding: FragmentStaffListingBinding
    private val nav by lazy { findNavController() }
    private val vm: StaffViewModel by activityViewModels()

    private lateinit var adapter: StaffAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentStaffListingBinding.inflate(inflater,container,false)
        binding.btnAddStaff.setOnClickListener { nav.navigate(R.id.addStaffFragment) }

        adapter = StaffAdapter()
        {
            holder, s ->  holder.root.setOnClickListener {nav.navigate(R.id.staffDetailFragment,
                bundleOf("id" to s.id))}
            holder.editBtn.setOnClickListener{nav.navigate(R.id.staffEditDetail,
                bundleOf("id" to s.id))}
            holder.deleteBtn.setOnClickListener{delete(s.id)}
        }
        load()

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        return binding.root
    }


    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun load() {
        // TODO: Load data
        vm.getAll().observe(viewLifecycleOwner){ staffs->
            adapter.submitList(staffs)
        }
    }

    private fun delete(id:String){
        Firebase.firestore
            .collection("staffs")
            .document(id)
            .delete()
    }


}