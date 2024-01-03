package my.com.teddyFood.Users

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.teddyFood.managerLogIn.Manager
import my.com.teddyFood.managerLogIn.ManagerViewModel
import my.com.teddyFood.cropToBlob
import my.com.teddyFood.databinding.FragmentUserProfileBinding
import my.com.teddyFood.errorDialog
import my.com.teddyFood.toBitmap


class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding
    private val nav by lazy { findNavController() }
    private val vm: ManagerViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        vm.getUserLiveData().observe(viewLifecycleOwner) { user ->
            getInfo(user)
            binding.btnSubmit.setOnClickListener { submit(user) }
            binding.btnReset.setOnClickListener { getInfo(user) }
        }

        binding.imgPhoto.setOnClickListener { select() }

        return binding.root
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun getInfo(user: Manager) {
        binding.txtId.setText(user.id)
        binding.imgPhoto.setImageBitmap(user.photo.toBitmap())
        binding.edtName.setText(user.name)
        binding.edtGmail.setText(user.email)
        binding.edtPassword.setText(user.password)
        binding.edtAge.setText(user.age.toString())

        binding.edtGmail.requestFocus()
    }

    private fun submit(user: Manager) {
        // TODO: Update (set)
        val v = Manager(
            id = user.id,
            email = binding.edtGmail.text.toString().trim(),
            password = binding.edtPassword.text.toString().trim(),
            name = binding.edtName.text.toString().trim(),
            age = binding.edtAge.text.toString().toIntOrNull() ?: 0,
            photo = binding.imgPhoto.cropToBlob(100,100),
        )

        val err = vm.validate(v, false)
        if(err != ""){
            errorDialog(err)
            return
        }

        vm.set(v)
        nav.navigateUp()
    }
}
