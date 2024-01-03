package my.com.teddyFood.Donation

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import my.com.teddyFood.databinding.FragmentThankUpdateBinding
import my.com.teddyFood.errorDialog

class ThankUpdateFragment : Fragment() {

    private lateinit var binding: FragmentThankUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: DonateViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore

    private val id by lazy { requireArguments().getString("id") ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentThankUpdateBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()

        reset()
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnDelete.setOnClickListener { delete() }

        return binding.root
    }

    private fun reset() {
        // TODO: Load data
        val d = vm.get(id)
        if(d == null) {
            nav.navigateUp()
            return
        }

        load(d)
    }

    private fun load(d: Donate) {
        binding.edtName.setText(d.name)
        binding.edtGmail.setText(d.email)
        binding.edtPhone.setText(d.phone)
        binding.edtType.setText(d.type)
        binding.edtQuantity.setText(d.quantity.toString())

        binding.edtName.requestFocus()
    }

    private fun delete() {
        // TODO: Delete
        vm.delete(id)
        nav.navigateUp()
    }

    private fun submit() {
        // TODO: Update (set)
        var newName = binding.edtName.text.toString().trim()
        var newGmail = binding.edtGmail.text.toString().trim()
        var newPhone = binding.edtPhone.text.toString().trim()
        var newType = binding.edtType.text.toString().trim()
        var newQuantity = binding.edtQuantity.text.toString().toIntOrNull() ?: 0

        db.collection("donate").whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
                updateInfo(id, newName, newGmail, newPhone, newType, newQuantity)
            }
    }

    private fun updateInfo(id: String, newName: String, newGmail: String, newPhone: String, newType: String, newQuantity: Int) {
        val refresh = db.collection("donate").document(id)
        refresh.update("name",newName)
        refresh.update("email",newGmail)
        refresh.update("phone",newPhone)
        refresh.update("type",newType)
        refresh.update("quantity",newQuantity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }

        val err = vm.validate1(newName, newGmail, newPhone, newType, newQuantity, false)
        if(err != ""){
            errorDialog(err)
            return
        }

        nav.navigateUp()
    }
}