package my.com.teddyFood.Donation

import com.google.firebase.firestore.DocumentId

data class Donate(
    @DocumentId
    var id : String = "",
    var name : String = "",
    var email : String = "",
    var phone : String = "",
    var type  : String = "",
    var quantity : Int = 0,
)