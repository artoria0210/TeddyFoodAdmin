package my.com.teddyFood.staffManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.teddyFood.databinding.FragmentStaffEditDetailBinding


class StaffEditDetail : Fragment() {
    private lateinit var binding: FragmentStaffEditDetailBinding
    private val nav by lazy { findNavController() }
    private val vm: StaffViewModel by activityViewModels()
    private val id by lazy { requireArguments().getString("id") ?: "" }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStaffEditDetailBinding.inflate(inflater, container, false)
        reset()
        binding.btnEditReset.setOnClickListener{ reset() }
        binding.btnEditSubmit.setOnClickListener { done() }

        return binding.root
    }



    private fun reset() {
        val s = vm.getID(id)
        if (s == null) {
            Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()
            nav.navigateUp()
            return
        }
        load(s)
    }

     private fun load(s: Staff) {

        binding.editID.text = s.id
        binding.editAge.text = s.age.toString()
        binding.editGender.text = s.gender
        binding.editName.text = s.name
         binding.addAddress.setText(s.address)
        binding.addPostcode.setText(s.postcode.toString())
        setState(s.state)
        binding.editTextPhone.setText(s.phone.toString())

    }

    private fun setState(state: String) {
        // TODO
        for (i in 0 until binding.spinner.count) {
            val value = binding.spinner.getItemAtPosition(i) as String
            if (value == state) {
                binding.spinner.setSelection(i)
                break
            }
        }
    }

    private fun done() {
        val address = binding.addAddress.text.toString()
        val postcode = binding.addPostcode.text.toString().toIntOrNull()?:0
        var valid:Boolean  = true
        if(address==""){
            toast("Invalid address")
            valid = false
        }

        if(postcode<10000||postcode>99999){
            toast("Invalid postcode")
            valid = false
        }

        if(valid){
            val s= Staff(
                id = id,
                name = binding.editName.text.toString().trim(),
                gender = binding.editGender.text.toString().trim(),
                age = binding.editAge.text.toString().toIntOrNull()?:0,
                address = binding.addAddress.text.toString().trim(),
                postcode = binding.addPostcode.text.toString().toIntOrNull()?:0,
                state = binding.spinner.selectedItem as String,
                phone = binding.editTextPhone.text.toString().toIntOrNull()?:0,
            )
            vm.set(s)
            nav.navigateUp()
        }





    }

    private fun toast(text: String) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }


}


