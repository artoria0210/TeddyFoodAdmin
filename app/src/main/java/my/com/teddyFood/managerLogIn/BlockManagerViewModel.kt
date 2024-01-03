package my.com.teddyFood.managerLogIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class BlockManagerViewModel:ViewModel() {
    private val col = Firebase.firestore.collection("blocklists")
    private val blockmanagers = MutableLiveData<List<BlockManager>>()

    init {
        col.addSnapshotListener{ snap, _->blockmanagers.value = snap?.toObjects()}
    }

    fun initialize() = null

    fun getUserBlockEmail(email: String): BlockManager?{
        return blockmanagers.value?.find { m->
            m.email == email
        }
    }
}