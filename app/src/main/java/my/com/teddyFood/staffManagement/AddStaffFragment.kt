package my.com.teddyFood.staffManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.com.teddyFood.R
import my.com.teddyFood.databinding.FragmentAddStaffBinding


class AddStaffFragment : Fragment() {

    private lateinit var binding: FragmentAddStaffBinding

    private val nav by lazy { findNavController()  }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentAddStaffBinding.inflate(inflater,container,false)

        binding.btnReset.setOnClickListener { reset() }
        binding.btnDone.setOnClickListener { done() }

        return binding.root

    }

    private fun done() {
        //input
        val name=binding.editStaffName.text.toString()
        val age=binding.editStaffAge.text.toString().toIntOrNull()?:0
        val gender= getGender()
        val address=binding.editStaffAddress.text.toString()
        val postcode=binding.editPostcode.text.toString().toIntOrNull()?:0
        val state=binding.spinState.selectedItem as String
        val phone=binding.editStaffPhone.text.toString().toIntOrNull()?:0
        val id= binding.editStaffId.text.toString()
        var valid:Boolean  = true


        //validation
        if (name==""){
            toast("Invalid name")
            valid = false
        }

        if(age<=0){
            toast("Invalid age")
            valid = false
        }

        if(address==""){
            toast("Invalid address")
            valid = false
        }

        if(postcode<10000||postcode>99999){
            toast("Invalid postcode")
            valid = false
        }

        if(id==""){
            toast("Invalid id")
            valid = false
        }

        if(valid){
            val s= Staff(
                id = binding.editStaffId.text.toString().trim().uppercase(),
                name = binding.editStaffName.text.toString().trim(),
                age = binding.editStaffAge.text.toString().toIntOrNull() ?:0,
                gender = getGender(),
                address = binding.editStaffAddress.text.toString().trim(),
                postcode = binding.editPostcode.text.toString().toIntOrNull() ?:0,
                state = binding.spinState.selectedItem as String,
                phone = binding.editStaffPhone.text.toString().toIntOrNull() ?:0,
            )
            Firebase.firestore
                .collection("staffs")
                .document(s.id)
                .set(s)

            nav.navigateUp()
        }

    }

    private fun toast(text: String) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }


    private fun getGender(): String {
        return when(binding.rgpStaffGender.checkedRadioButtonId){
            R.id.radStaffMale -> "Male"
            R.id.radStaffFemale ->"Female"
            else -> ""
        }
    }

    private fun reset() {
        binding.editStaffName.text.clear()
        binding.editStaffAge.text.clear()
        binding.rgpStaffGender.check(R.id.radStaffMale)
        binding.editStaffAddress.text.clear()
        binding.editPostcode.text.clear()
        binding.spinState.setSelection(0)
        binding.editStaffPhone.text.clear()
        binding.editStaffId.text.clear()
    }

}