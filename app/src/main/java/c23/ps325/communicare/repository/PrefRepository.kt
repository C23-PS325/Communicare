package c23.ps325.communicare.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PrefRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val NAMEKEY = stringPreferencesKey("name")
    private val STATUS = booleanPreferencesKey("status")
    private val THEME_KEY = booleanPreferencesKey("theme_setting")

    fun getName(): Flow<String> = dataStore.data.map { pref ->
        pref[NAMEKEY] ?: ""
    }

    fun getStatus(): Flow<Boolean> = dataStore.data.map { pref ->
        pref[STATUS] ?: false
    }

    fun getThemeSetting(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: false
    }

    suspend fun saveLogin(isLogin : Boolean, isName: String) {
        dataStore.edit { preferences ->
            preferences[STATUS] = isLogin
            preferences[NAMEKEY] = isName
        }
    }

    suspend fun saveTheme(isDarkModeActive : Boolean){
        dataStore.edit { theme -> theme[THEME_KEY] = isDarkModeActive }
    }

    suspend fun delete(){
        dataStore.edit { pref -> pref.clear() }
    }
}