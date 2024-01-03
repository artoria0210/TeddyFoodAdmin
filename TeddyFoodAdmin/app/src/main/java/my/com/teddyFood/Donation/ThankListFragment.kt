package my.com.teddyFood.Donation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import my.com.teddyFood.R
import my.com.teddyFood.databinding.FragmentThankListBinding

class ThankListFragment : Fragment() {

    private lateinit var binding: FragmentThankListBinding
    private val nav by lazy { findNavController() }
    private val vm: DonateViewModel by activityViewModels()

    private lateinit var adapter: DonateAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentThankListBinding.inflate(inflater, container, false)

        binding.btnInsert.setOnClickListener { nav.navigate(R.id.donationFragment) }
        binding.btnDeleteAll.setOnClickListener { deleteAll() }

        adapter = DonateAdapter() { holder, donate ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.thankUpdateFragment, bundleOf("id" to donate.id))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener { delete(donate.id) }
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // TODO: Load data
        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.txtCount.text = "${list.size} donator(s)"
        }

        return binding.root
    }

    private fun deleteAll() {
        // TODO: Delete all
        vm.deleteAll()
    }

    private fun delete(id: String) {
        // TODO: Delete
        vm.delete(id)
    }
}