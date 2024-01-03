package my.com.teddyFood.Donation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.teddyFood.databinding.FragmentDonationBinding
import my.com.teddyFood.errorDialog

class DonationFragment : Fragment() {

    private lateinit var binding: FragmentDonationBinding
    private val nav by lazy { findNavController() }
    private val vm: DonateViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDonationBinding.inflate(inflater, container, false)

        reset()
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }

        return binding.root
    }

    private fun reset() {
        binding.edtName.text.clear()
        binding.edtType.text.clear()
        binding.edtGmail.text.clear()
        binding.edtPhone.text.clear()
        binding.edtQuantity.text.clear()

        binding.edtName.requestFocus()
    }

    private fun submit() {
        // TODO: Insert (set)
        val d = Donate(
            id = "",
            name  = binding.edtName.text.toString().trim(),
            email  = binding.edtGmail.text.toString().trim(),
            phone  = binding.edtPhone.text.toString().trim(),
            type  = binding.edtType.text.toString().trim(),
            quantity   = binding.edtQuantity.text.toString().toIntOrNull() ?: 0,
        )


        val err = vm.validate(d, false)
        if(err != ""){
            errorDialog(err)
            return
        }

        vm.set(d)
        nav.navigateUp()
    }
}