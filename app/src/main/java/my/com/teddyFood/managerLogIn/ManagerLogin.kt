package my.com.teddyFood.managerLogIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import my.com.teddyFood.R
import my.com.teddyFood.databinding.FragmentManagerLoginBinding
import my.com.teddyFood.hideKeyboard


class ManagerLogin : Fragment() {
    private lateinit var binding: FragmentManagerLoginBinding
    private val nav by lazy { findNavController() }
    private val mgv : ManagerViewModel by activityViewModels()
    var checkTimer:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentManagerLoginBinding.inflate(inflater, container, false)
        binding.btnLogin.setOnClickListener { login() }
        return binding.root
    }

    private fun login(){
        hideKeyboard()

        val ctx = requireContext()
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()
        val remember= binding.loginCheckBox.isChecked


        lifecycleScope.launch{
            val success = mgv.login(ctx,email, password, remember)
            val checkEmail =mgv.checkEmail(ctx,email)
            val checkBlock =mgv.checkBlock(ctx,email)


                if(checkEmail){
                    if(!checkBlock){
                    if(checkTimer<=3){
                        if(success){
                            Toast.makeText(context, "Valid", Toast.LENGTH_SHORT).show()
                            nav.navigate(R.id.homeFragment)
                        }
                        else{
                            Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show()
                            checkTimer++
                        }
                    }
                    else if(checkTimer>3){
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        checkTimer = 0
                        val m = mgv.getUserEmail(email)

                        if (m != null) {
                            add(m)
                        }
                        else{
                            Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()
                            nav.navigateUp()
                        }
                    }

                }
                else
                    Toast.makeText(context, "Your Acc has been Block", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(context, "No Email Available", Toast.LENGTH_SHORT).show()

        }
    }

    private fun add(m: Manager){
        val email    = m.email
        val password = m.password
        val name     = m.name
        val age      = m.age
        val photo    = m.photo

        Manager(
            email    = email,
            password = password,
            name     = name,
            age      = age,
            photo    = photo,
        )

        Firebase.firestore
            .collection("blocklists")
            .document(m.id)
            .set(m)
        nav.navigateUp()

    }


    private fun toast(text: String) {
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show()
    }
}