package volunteerManagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class VolunteerViewModel : ViewModel() {
    private val col = Firebase.firestore.collection("volunteers")
    private val volunteers = MutableLiveData<List<Volunteer>>()

    private var result = listOf<Volunteer>()
    private var searchName = ""       // Search
    private var field = ""      // Sort
    private var reverse = false // Sort4

    init {
        viewModelScope.launch{
            col.addSnapshotListener{ snap, _->
                if (snap == null) return@addSnapshotListener

                //events.value = snap?.toObjects()
                result = snap.toObjects<Volunteer>()

                updateResult()
            }
        }
    }

    private fun updateResult() {
        var list = result

        // Search + filter
        list = list.filter { e ->
            e.name.contains(searchName, true)
        }

        // Sort
        list = when (field) { //not be affected
            "name"    -> list.sortedBy { e -> e.name }
            "email"  -> list.sortedBy { e -> e.email }
            else    -> list
        }

        if(reverse) list = list.reversed()

        volunteers.value = list
    }

    //Set [title] -> update result
    fun search(name: String) {
        this.searchName = name
        updateResult()
    }


    //Set [field] and [reverse] -> update result
    fun sort(field: String): Boolean {
        reverse = if(this.field == field) !reverse else false
        this.field = field
        updateResult()

        return reverse
    }

    fun get(name: String): Volunteer? {
        // TODO
        return null
    }

    fun getID(id:String): Volunteer?{
        return volunteers.value?.find { s->s.id==id }
    }

    fun getAll() = volunteers

    fun delete(id: String,name: String) {
        // TODO
        col.document(id).delete()
        col.document(name).delete()
    }

    fun deleteAll() {
        // TODO
        volunteers.value?.forEach { s->delete(s.id,s.name) }
    }

    fun set(s: Volunteer) {
        // TODO
        col.document(s.id).set(s)

    }
}