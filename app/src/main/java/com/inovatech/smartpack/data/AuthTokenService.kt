package com.inovatech.smartpack.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val TOKEN_STORAGE = "token_storage"
    private val TOKEN_KEY = "token_key"

    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val storage = EncryptedSharedPreferences.create(
        context,
        TOKEN_STORAGE,
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAuthToken(token: String) {
        storage.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getAuthToken(): String? {
        return storage.getString(TOKEN_KEY, null)
    }

    fun clearAuthToken() {
        storage.edit().remove(TOKEN_KEY).apply()
    }

    fun isTokenValid(): Boolean {
        val token = this.getAuthToken()
        //TODO Implantar validació del token

        return false
    }
}