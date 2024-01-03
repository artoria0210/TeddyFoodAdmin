package my.com.teddyFood.staffManagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class StaffViewModel:ViewModel() {
    private val col = Firebase.firestore.collection("staffs")
    private val staffs = MutableLiveData<List<Staff>>()

    init {
        col.addSnapshotListener{ snap, _->staffs.value = snap?.toObjects()}
    }

    fun get(id: String,name: String): Staff? {
        // TODO
        return null
    }

    fun getID(id:String): Staff?{
        return staffs.value?.find { s->s.id==id }
    }

    fun getAll() = staffs

    fun delete(id: String,name: String) {
        // TODO
        col.document(id).delete()
        col.document(name).delete()
    }

    fun deleteAll() {
        // TODO
        staffs.value?.forEach { s->delete(s.id,s.name) }
    }

    fun set(s: Staff) {
        // TODO
        col.document(s.id).set(s)

    }

    private fun idExists (id: String): Boolean{

        return staffs.value?.any{s -> s.id==id}?:false
    }

}