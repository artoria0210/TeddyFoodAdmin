package my.com.teddyFood.managerLogIn

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import my.com.teddyFood.databinding.FragmentForgetBinding
import my.com.teddyFood.hideKeyboard
import java.text.DecimalFormat


class ForgetFragment : Fragment() {
    private lateinit var binding: FragmentForgetBinding
    private val nav by lazy { findNavController() }
    private val vm: ManagerViewModel by activityViewModels()

    var num = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgetBinding.inflate(inflater, container, false)
        binding.btnSend.setOnClickListener { send() }
        binding.btnForgetSub.setOnClickListener { resetPass(num) }
        vm.initialize()

        return binding.root
    }
    // TODO: Compose and send email
    private fun send() {
        hideKeyboard()

        val email = binding.editForgetEmail.text.toString().trim()
        if(!isEmail(email)){
            snackbar("Invalid e-mail")
            binding.editForgetEmail.requestFocus()
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
                binding.btnSend.isEnabled=true
                binding.editForgetEmail.requestFocus()
            }
        snackbar("Sending...")
        binding.btnSend.isEnabled=false
    }

    private fun resetPass(num:String){
        hideKeyboard()
        val otp = binding.editOTP.text.toString().trim()
        val email = binding.editReEmail.text.toString().trim()
        if (otp == num){
            val s = vm.getUserEmail(email)
 //           Log.i("Test","s")
            if (s == null) {
                nav.navigateUp()
                return
            }
            view(s)
        }
        else
            snackbar("Wrong OTP ,Pls Resend your email")
    }

    private fun view(s: Manager){
        binding.passView.text = s.password
    }

    // TODO: Validate if email address valid
    private fun isEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()


    private fun snackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }

}