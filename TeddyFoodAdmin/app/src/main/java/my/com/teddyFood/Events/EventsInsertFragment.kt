package my.com.teddyFood.Events

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.teddyFood.cropToBlob
import my.com.teddyFood.databinding.FragmentEventsInsertBinding
import my.com.teddyFood.errorDialog

class EventsInsertFragment : Fragment() {

    private lateinit var binding: FragmentEventsInsertBinding
    private val nav by lazy { findNavController() }
    private val vm: EventsViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentEventsInsertBinding.inflate(inflater, container, false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }

        return binding.root
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        binding.edtId.text.clear()
        binding.edtTitle.text.clear()
        binding.edtDescription.text.clear()
        binding.edtMap.text.clear()
        binding.imgPhoto.setImageDrawable(null)
        binding.edtId.requestFocus()
    }

    private fun submit() {
        // TODO: Insert (set)
        val e = Event(
            id = binding.edtId.text.toString().trim().uppercase(),
            title  = binding.edtTitle.text.toString().trim(),
            description   = binding.edtDescription.text.toString().trim(),
            map   = binding.edtMap.text.toString().trim(),
            photo = binding.imgPhoto.cropToBlob(500, 500)
        )


        val err = vm.validate(e)
        if(err != ""){
            errorDialog(err)
            return
        }

        vm.set(e)
        nav.navigateUp()
    }
}