package my.com.teddyFood.Events

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.teddyFood.cropToBlob
import my.com.teddyFood.databinding.FragmentEventsUpdateBinding
import my.com.teddyFood.errorDialog
import my.com.teddyFood.toBitmap
import java.util.*

class EventsUpdateFragment : Fragment() {

    private lateinit var binding: FragmentEventsUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: EventsViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id") ?: "" }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentEventsUpdateBinding.inflate(inflater, container, false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnDelete.setOnClickListener { delete() }
        binding.btnMap.setOnClickListener { openMap() }

        return binding.root
    }


    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        // TODO: Load data
        val f = vm.get(id)
        if(f == null) {
            nav.navigateUp()
            return
        }

        load(f)
    }

    private fun load(e: Event) {
        binding.txtId.text = e.id
        binding.edtTitle.setText(e.title)
        binding.edtDescription.setText(e.description)
        binding.edtMap.setText(e.map)

        // TODO: Load photo and date
        binding.imgPhoto.setImageBitmap(e.photo.toBitmap())

        binding.edtTitle.requestFocus()
    }

    private fun submit() {
        // TODO: Update (set)
        val e = Event(
            id = id,
            title = binding.edtTitle.text.toString().trim(),
            description = binding.edtDescription.text.toString().trim(),
            map = binding.edtMap.text.toString().trim(),
            photo = binding.imgPhoto.cropToBlob(500, 500)
        )

        val err = vm.validate(e, false)
        if(err != ""){
            errorDialog(err)
            return
        }

        vm.set(e)
        nav.navigateUp()
    }

    private fun delete() {
        // TODO: Delete
        vm.delete(id)
        nav.navigateUp()
    }

    private fun openMap() {
        // TODO(4)
        val map = binding.edtMap.text.toString().trim()
        //val uri = Uri.parse("geo:0,0?q=3.216315, 101.728277")
        val uri = Uri.parse("geo:0,0?q=$map")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}