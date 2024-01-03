package my.com.teddyFood.managerLogIn

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.MimeMessage


data class Manager(
    @DocumentId
    var id       :String = "",
    var email    :String = "",
    var password :String = "",
    var name     :String = "",
    var age      :Int    = 0,
    var photo: Blob = Blob.fromBytes(ByteArray(0)),
)

data class BlockManager(
    @DocumentId
    var id       :String = "",
    var email    :String = "",
    var password :String = "",
    var name     :String = "",
    var age      :Int    = 0,
    var photo: Blob = Blob.fromBytes(ByteArray(0)),
)

val MANAGER = Firebase.firestore.collection("users")
val BLOCKMANAGER = Firebase.firestore.collection("blocklists")


class SimpleEmail(
    private var to     : String  = "",
    private var subject: String  = "",
    private var content: String  = "",
    private var isHtml : Boolean = false,
){
    private val username = "jokerchin0319@gmail.com"
    private val pass = "Fuckboyhee001!"
    private val personal = "MAD Assignment"

    private val host = "smtp.gmail.com"
    private val port = "587"

    private val from = "$personal<$username>"
    private var message: MimeMessage? = null

    fun to(to: String): SimpleEmail {
        this.to = to
        return this
    }

    fun subject(subject: String): SimpleEmail {
        this.subject = subject
        return this
    }

    fun content(content: String): SimpleEmail {
        this.content = content
        return this
    }

    fun isHtml(isHtml: Boolean = true): SimpleEmail {
        this.isHtml = isHtml
        return this
    }

    private fun getMessage(): MimeMessage {
        if (message == null) {
            val prop = Properties()
            prop["mail.smtp.host"] = host
            prop["mail.smtp.port"] = port
            prop["mail.smtp.starttls.enable"] = "true"
            prop["mail.smtp.auth"] = "true"

            val auth = object : Authenticator() {
                override fun getPasswordAuthentication() = PasswordAuthentication(username, pass)
            }

            val sess = Session.getDefaultInstance(prop, auth)

            message = MimeMessage(sess)
        }

        return message!!
    }

    fun send(callback: () -> Unit = {}) {
        val type = if (isHtml) "text/html;charset=utf-8" else "text/plain;charset=utf-8"

        val msg = getMessage()
        msg.setFrom(from)
        msg.setRecipients(Message.RecipientType.TO, to)
        msg.setSubject(subject)
        msg.setContent(content, type)

        CoroutineScope(Dispatchers.IO).launch {
            // NOTE: Use try-catch-finally block to silent runtime error
            Transport.send(msg)
            withContext(Dispatchers.Main) { callback() }
        }
    }
}







