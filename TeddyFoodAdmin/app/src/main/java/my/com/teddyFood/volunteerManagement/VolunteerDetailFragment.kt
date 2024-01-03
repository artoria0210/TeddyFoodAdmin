package volunteerManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.teddyFood.databinding.FragmentVolunteerDetailBinding
import my.com.teddyFood.toBitmap


class VolunteerDetailFragment : Fragment() {

    private lateinit var binding: FragmentVolunteerDetailBinding
    private val nav by lazy { findNavController() }
    private val vm: VolunteerViewModel by activityViewModels()
    private val id by lazy { requireArguments().getString("id") ?: "" }

    private lateinit var adapter: VolunteerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVolunteerDetailBinding.inflate(inflater,container, false)
        adapter = VolunteerAdapter()
        reset()
        return binding.root
    }

    private fun reset(){
        val s = vm.getID(id)
        if(s == null) {
            Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()
            nav.navigateUp()
            return
        }
        load(s)
    }

    private fun load(s: Volunteer){
        binding.volonViewEmail.text=s.email
        binding.volonViewName.text=s.name
        binding.volonViewUserName.text=s.username
        binding.volunImage.setImageBitmap(s.photo.toBitmap())
    }

}