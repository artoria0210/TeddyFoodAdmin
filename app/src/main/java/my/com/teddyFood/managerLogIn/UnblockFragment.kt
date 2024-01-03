package my.com.teddyFood.managerLogIn

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import my.com.teddyFood.databinding.FragmentUnblockBinding
import my.com.teddyFood.hideKeyboard
import java.text.DecimalFormat


class UnblockFragment : Fragment() {
    private lateinit var binding: FragmentUnblockBinding
    private val nav by lazy { findNavController() }
    private val vm: ManagerViewModel by activityViewModels()
    private val bm: BlockManagerViewModel by activityViewModels()
    var num = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentUnblockBinding.inflate(inflater, container, false)
        binding.btnUnblockSent.setOnClickListener{ send() }
        binding.btnUnblockSubmit.setOnClickListener { submit(num) }
        vm.initialize()
        bm.initialize()
        return binding.root
    }

    private fun send(){
        hideKeyboard()
        val email = binding.editUnblockEmail.text.toString().trim()
        if(!isEmail(email)){
            snackbar("Invalid e-mail")
            binding.editUnblockEmail.requestFocus()
            return
        }
        val n = (0..9999).random()
        val fmt = DecimalFormat("0000")
        val number = fmt.format(n)
        num = number

        val subject = "OTP - ${System.currentTimeMillis()}"
        val content = """
            <p>Your <b>OTP is:</b> </p>
            <h1 style="color: blue">$number </h1>
            <p>Thank You Very Much</p>
        """.trimIndent()

        SimpleEmail()
            .to(email)
            .subject(subject)
            .content(content)
            .isHtml()
            .send(){

                snackbar("Sent")
                binding.btnUnblockSent.isEnabled=true
                binding.editUnblockEmail.requestFocus()
            }
        snackbar("Sending...")
        binding.btnUnblockSent.isEnabled=false
    }

    private fun submit(num:String){
        hideKeyboard()
        val otp = binding.editUnblockOtp.text.toString().toIntOrNull()?:-1
        val email = binding.editUnblockAppEmail.text.toString().trim()
        val ctx = requireContext()

                lifecycleScope.launch{
                    if (otp.toString() == num){
                        val checkBlock =vm.checkBlock(ctx,email)
                        val s = bm.getUserBlockEmail(email)
                        if(checkBlock){
                            if (s != null) {
                                unblock(s)
                                nav.navigateUp()
                            }
                            else
                                nav.navigateUp()

                        }else
                            snackbar("No Acc been block")
                    }
                    else
                        snackbar("Wrong OTP ,Pls Resend your email")
                }

    }

    private fun unblock(s: BlockManager){
        Firebase.firestore
            .collection("blocklists")
            .document(s.id)
            .delete()

    }

    // TODO: Validate if email address valid
    private fun isEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()


    private fun snackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }
}