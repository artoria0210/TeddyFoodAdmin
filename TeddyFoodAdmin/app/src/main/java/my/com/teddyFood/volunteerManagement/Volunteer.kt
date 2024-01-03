package volunteerManagement

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Blob

data class Volunteer (
    @DocumentId
    var id: String="",
    var email: String = "",
    var name : String = "",
    var username: String="",
    var password: String="",
    var photo : Blob = Blob.fromBytes(ByteArray(0)),
    )