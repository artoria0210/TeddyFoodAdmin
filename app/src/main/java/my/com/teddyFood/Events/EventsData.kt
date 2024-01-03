package my.com.teddyFood.Events

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId

data class Event(
    @DocumentId
    var id : String = "",
    var title : String = "",
    var description : String = "",
    var map : String = "",
    var photo: Blob = Blob.fromBytes(ByteArray(0)),
)