package com.nahid.book_orbit.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreference(private val dataStore: DataStore<Preferences>) {
    companion object{
        val TOKEN = stringPreferencesKey("token")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val MODE = booleanPreferencesKey("mode")

    }

    suspend fun saveToken(token:String):Boolean{
       return try {
            dataStore.edit {
                it[TOKEN] = token
            }
            true
        }catch (e:Exception){
           return false
        }
    }

    fun readToken():Flow<String>{
        return dataStore.data.map {
            it[TOKEN] ?: ""
        }
    }
    
    suspend fun saveUserName(name:String):Boolean{
       return try {
            dataStore.edit {
                it[USER_NAME] = name
            }
            true
        }catch (e:Exception){
           return false
        }
    }

    fun readUserName():Flow<String>{
        return dataStore.data.map {
            it[USER_NAME] ?: ""
        }
    }

    suspend fun saveRefreshToken(refreshToken: String):Boolean {
        return try {
            dataStore.edit {
                it[REFRESH_TOKEN] = refreshToken
            }
            true
        }catch (e:Exception){
            return false
        }
    }

    fun readRefreshToken():Flow<String>{
        return dataStore.data.map {
            it[REFRESH_TOKEN] ?: ""
        }
    }

    suspend fun saveUserId(userId:String):Boolean{
        return try {
            dataStore.edit {
                it[USER_ID] = userId
            }
            true
        }catch (e:Exception){
            return false
        }
    }

    fun readUserId():Flow<String>{
        return dataStore.data.map {
            it[USER_ID] ?: ""
        }
    }

    suspend fun isDarkMode(isDark:Boolean):Boolean{
        return try {
            dataStore.edit {
                it[MODE] = isDark
            }
            true
        }catch (e:Exception){
            return false
        }
    }

    fun readIsDarkMode():Flow<Boolean>{
        return dataStore.data.map {
            it[MODE] ?: false
        }
    }
}