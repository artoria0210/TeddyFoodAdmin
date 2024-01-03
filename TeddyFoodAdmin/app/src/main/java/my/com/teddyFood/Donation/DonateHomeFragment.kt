package my.com.teddyFood.Donation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import my.com.teddyFood.R
import my.com.teddyFood.cropToBlob
import my.com.teddyFood.databinding.FragmentDonateHomeBinding

class DonateHomeFragment : Fragment() {

    private lateinit var binding: FragmentDonateHomeBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDonateHomeBinding.inflate(inflater, container, false)

        binding.btnFoodDonate.setOnClickListener { nav.navigate(R.id.donationFragment) }
        binding.btnList.setOnClickListener { nav.navigate(R.id.thankListFragment) }

        binding.imgDonate.setImageResource(R.drawable.logo_donate)
        binding.imgDonate.cropToBlob(400, 400)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(nav) || super.onOptionsItemSelected(item)
    }
}