package my.com.teddyFood.Events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class EventsViewModel : ViewModel() {
    private val col = Firebase.firestore.collection("events")
    private val events = MutableLiveData<List<Event>>()

    private var result = listOf<Event>()
    private var searchTitle = ""       // Search
    private var searchId = "" // Filter
    private var field = ""      // Sort
    private var reverse = false // Sort

    init {

        viewModelScope.launch {
            col.addSnapshotListener { snap, _ ->
                if (snap == null) return@addSnapshotListener

                //events.value = snap?.toObjects()
                result = snap.toObjects<Event>()

                updateResult()
            }
        }
    }

    //search and filter
    private fun updateResult() {
        var list = result

        // Search + filter
        list = list.filter { e ->
            e.title.contains(searchTitle, true)
        }

        // Sort
        list = when (field) { //not be affected
            "id"    -> list.sortedBy { e -> e.id }
            "title"  -> list.sortedBy { e -> e.title }
            else    -> list
        }

        if(reverse) list = list.reversed()

        events.value = list
    }

    //Set [title] -> update result
    fun search(title: String) {
        this.searchTitle = title
        updateResult()
    }

    //Set [id] -> update result
    fun filter(id: String) {
        this.searchId = id
        updateResult()
    }

    //Set [field] and [reverse] -> update result
    fun sort(field: String): Boolean {
        reverse = if(this.field == field) !reverse else false
        this.field = field
        updateResult()

        return reverse
    }

    fun get(id: String): Event?{
        //return events.value?.find { e -> e.id == id}
        return result.find { e -> e.id == id}
    }

    fun getAll() = events

    fun delete(id: String) {
        // TODO
        col.document(id).delete()
    }

    fun deleteAll() {
        // TODO
        //col.get().addOnSuccessListener { snap -> snap.documents.forEach { doc -> delete(doc.id)} }

        //events.value?.forEach { e -> delete(e.id) }
        result.forEach { e -> delete(e.id) }
    }

    fun set(e: Event) {
        // TODO
        col.document(e.id).set(e)
    }

    private fun idExists(id: String): Boolean{
        //return events.value?.any{ e -> e.id == id } ?: false
        return result.any{ e -> e.id == id } ?: false
    }

    private fun titleExists(title: String): Boolean{
        //return events.value?.any{ e -> e.title == title } ?: false
        return result.any{ e -> e.title == title } ?: false
    }

    fun validate(e: Event, insert: Boolean = true): String {
        val regexId = Regex("""^[0-9A-Z]{4}$""")
        var err = ""

        if (insert) {
            err += if (e.id == "") "- Id is required.\n"
            else if (!e.id.matches(regexId)) "- Id format is invalid.\n"
            else if (idExists(e.id)) "- Id is duplicated.\n"
            else ""
        }

        err += if (e.title == "") "- Title is required.\n"
        else if (e.title.length < 3) "- Title is too short.\n"
        else ""

        err += if (e.description == "") "- Description is required.\n"
        else ""

        err += if (e.map == "") "- Map is required.\n"
        else ""

        err += if (e.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return err
    }
}