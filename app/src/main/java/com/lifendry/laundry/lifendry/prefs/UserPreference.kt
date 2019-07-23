package com.lifendry.laundry.lifendry.prefs

import android.content.Context
import android.content.SharedPreferences
import com.lifendry.laundry.lifendry.model.user.User


class UserPreference(context: Context) {
    private val preferences: SharedPreferences
    var user: User?
        get() {
            if (preferences.getString((ID), null).isNullOrEmpty()) {
                return null
            } else {
                val model = User()
                model.id = preferences.getString(ID, "")
                model.email = preferences.getString(EMAIL, "")
                model.name = preferences.getString(NAME, "")
                model.roleId = preferences.getInt(ROLE_ID, 0)
                return model
            }

        }
        set(value) {
            val editor = preferences.edit()
            editor.putString(NAME, value?.name)
            editor.putString(EMAIL, value?.email)
            value?.roleId?.let { editor.putInt(ROLE_ID, it) }
            editor.putString(ID, value?.id)
            editor.apply()
        }

    init {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private val PREFS_NAME = "user_pref"
        private val ID = "id"
        private val EMAIL = "email"
        private val NAME = "name"
        private val ROLE_ID = "roleid"


    }
}