package ade.test.danamon.data.pref

import ade.test.danamon.base.BaseSharedPreferences
import ade.test.danamon.domain.model.User
import android.content.Context
import android.util.Base64
import com.google.gson.Gson

class UserPref(context: Context) : BaseSharedPreferences(context) {

    var user: User?
        get() {
            val json = getString(KEY, "")
            return Gson().fromJson(json, User::class.java)?.apply {
                this.token = Base64.decode(this.token, Base64.DEFAULT).decodeToString()
            }
        }
        set(value) {
            value?.apply {
                this.token = Base64.encodeToString(this.token.toByteArray(), Base64.DEFAULT)
            }
            val json = Gson().toJson(value)
            set(KEY, json)
        }

    fun remove() {
        remove(KEY)
    }

    companion object {
        private const val KEY = "USER_KEY"
    }
}