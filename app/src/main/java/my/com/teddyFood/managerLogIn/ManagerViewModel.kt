package my.com.teddyFood.managerLogIn

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ManagerViewModel:ViewModel() {
    private val col = Firebase.firestore.collection("users")
    private val managerLiveData = MutableLiveData<Manager>()
    private val managers = MutableLiveData<List<Manager>>()
    private var listener: ListenerRegistration? =null

    init {
        col.addSnapshotListener{ snap, _->managers.value = snap?.toObjects()}
    }

    fun initialize() = null

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

    // Return observable live data
    fun getUserLiveData(): LiveData<Manager> {
        return managerLiveData
    }

    // Return user from live data
    fun getUser(): Manager? {
        return managerLiveData.value
    }


    fun getUserEmail(email: String): Manager? {
        return managers.value?.find{ m ->
            m.email == email
        }
    }




    suspend fun checkEmail(ctx: Context,email: String):Boolean{
        val manager = MANAGER
            .whereEqualTo("email",email)
            .get()
            .await()
            .toObjects<Manager>()
            .firstOrNull()?: return false
        return true
    }

    suspend fun checkBlock(ctx: Context,email: String):Boolean{
        val block = BLOCKMANAGER
            .whereEqualTo("email",email)
            .get()
            .await()
            .toObjects<Manager>()
            .firstOrNull()?:return false
        return true
    }

    suspend fun login(ctx: Context, email: String, password: String, remember: Boolean = false):Boolean {
        // TODO(1A): Get the user record with matching email + password
        //           Return false is no matching found

        val manager = MANAGER
            .whereEqualTo("email",email)
            .whereEqualTo("password",password)
            .get()
            .await()
            .toObjects<Manager>()
            .firstOrNull() ?: return false

        // TODO(1B): Setup snapshot listener
        //           Update live data -> user

        listener?.remove()
        listener = MANAGER.document(manager.id).addSnapshotListener{ doc, _->
            managerLiveData.value=doc?.toObject()

        }

        // TODO(6A): Handle remember-me -> add shared preferences
        if(remember){
            getPreferences(ctx)
                .edit()
                .putString("email",email)
                .putString("password",password)
                .apply()
        }
        return true
    }

    // TODO(2): Logout
    @SuppressLint("NullSafeMutableLiveData")
    fun logout(ctx: Context) {
        // TODO(2A): Remove snapshot listener
        //           Update live data -> null
        listener?.remove()
        managerLiveData.value = null



        // TODO(6B): Handle remember-me -> clear shared preferences
        getPreferences(ctx).edit().clear().apply()

    }

    // TODO(6): Get shared preferences
    private fun getPreferences(ctx: Context): SharedPreferences {

        return EncryptedSharedPreferences.create(
            "Auth",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            ctx,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // TODO(7): Auto login from shared preferences
    suspend fun loginFromPreferences(ctx: Context) {
        val pref = getPreferences(ctx)
        val email = pref.getString("email",null)
        val password = pref.getString("password",null)

        if(email!=null && password != null){
            login(ctx, email, password)
        }

    }

    fun set(m: Manager) {
        // TODO
        col.document(m.id).set(m)

    }

    fun validate(u: Manager, insert: Boolean = true): String {
        var err = ""

        err += if (u.name == "") "- Name is required.\n"
        else if (u.name.length < 3) "- Name is too short.\n"
        else ""

        err += if (u.email == "") "- Email is required.\n"
        else if (u.email.length < 3) "- Email is too short.\n"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(u.email).matches()) "- Email is invalid.\n"
        else ""

        err += if (u.password == "") "- Password is required.\n"
        else if (u.password.length < 3) "- Password is too short.\n"
        else ""

        err += if (u.age == 0) "- Age is required.\n"
        else if (u.age < 18) "- Underage.\n"
        else ""

        err += if (u.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return err
    }


}