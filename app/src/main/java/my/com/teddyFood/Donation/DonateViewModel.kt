package my.com.teddyFood.Donation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DonateViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("donate")
    private val donation = MutableLiveData<List<Donate>>()

    init {
        col.addSnapshotListener {
                snap, d ->  //donation.value = snap?.toObjects()
            if (d != null) return@addSnapshotListener
            snap?.let {
                val allDonate = ArrayList<Donate>()
                val documents = snap.documents
                documents.forEach {
                    val donator = it.toObject<Donate>()
                    donator?.let {
                        allDonate.add(it)
                    }
                }
                donation.value = allDonate
            }
        }
    }

    fun get(id: String): Donate?{
        return donation.value?.find { d -> d.id == id}
    }

    fun getAll() = donation

    fun delete(id: String) {
        // TODO
        col.document(id).delete()
    }

    fun deleteAll() {
        // TODO
        //col.get().addOnSuccessListener { snap -> snap.documents.forEach { doc -> delete(doc.id)} }
        donation.value?.forEach { d -> delete(d.id) }
    }

    fun set(d: Donate) {
        // TODO
        val task = col.add(d)
        task.addOnSuccessListener {
            d.id = it.id
        }
        //col.document(d.id).set(d)
    }

    fun validate1(newName: String, newGmail: String, newPhone: String, newType: String, newQuantity: Int, insert: Boolean = true): String {
        var err = ""

        err += if (newName == "") "- Name is required.\n"
        else if (newName.length < 3) "- Name is too short.\n"
        else ""

        err += if (newGmail == "") "- Email is required.\n"
        else if (newGmail.length < 3) "- Email is too short.\n"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newGmail).matches()) "- Email is invalid.\n"
        else ""

        err += if (newType == "") "- Type is required.\n"
        else ""

        err += if (newPhone == "") "- Phone is required.\n"
        else if (newPhone.length < 10 || newPhone.length > 11) "- Phone is too short.\n"
        else ""

        err += if (newQuantity == 0) "- Quantity is required.\n"
        else ""

        return err
    }

    fun validate(d: Donate, insert: Boolean = true): String {
        var err = ""

        err += if (d.name == "") "- Name is required.\n"
        else if (d.name.length < 3) "- Name is too short.\n"
        else ""

        err += if (d.email == "") "- Email is required.\n"
        else if (d.email.length < 3) "- Email is too short.\n"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(d.email).matches()) "- Email is invalid.\n"
        else ""

        err += if (d.type == "") "- Type is required.\n"
        else ""

        err += if (d.phone == "") "- Phone is required.\n"
        else if (d.phone.length < 10 || d.phone.length > 11) "- Phone is too short.\n"
        else ""

        err += if (d.quantity == 0) "- Quantity is required.\n"
        else ""

        return err
    }
}