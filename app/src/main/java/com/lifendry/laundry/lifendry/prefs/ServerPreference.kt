package com.lifendry.laundry.lifendry.prefs

import android.content.Context
import android.content.SharedPreferences
import com.lifendry.laundry.lifendry.model.server.Server
import com.lifendry.laundry.lifendry.model.user.User


class ServerPreference(context: Context) {
    private val preferences: SharedPreferences
    var server: Server?
        get() {
            if (preferences.getInt((ID), 0) == 0) {
                return null
            } else {
                val model = Server()
                model.idServer = preferences.getInt(ID, 0)
                model.ip = preferences.getString(IP, "")
                return model
            }

        }
        set(value) {
            val editor = preferences.edit()
            value?.idServer?.let { editor.putInt(ID, it) }
            editor.putString(IP, value?.ip)
            editor.apply()
        }

    init {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private val PREFS_NAME = "server_pref"
        private val ID = "id_server"
        private val IP = "ip"


    }
}