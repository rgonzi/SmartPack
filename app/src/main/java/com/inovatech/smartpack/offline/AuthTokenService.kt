package com.inovatech.smartpack.offline

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  Singletó que gestiona el token en un EncryptedSharedPreferences. Utilitza una clau cifrada de
 *  tipus AES256 per a garantir la seguritat del token.
 */
@Singleton
class AuthTokenService @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val TOKEN_STORAGE = "token_storage"
    private val TOKEN_KEY = "token_key"

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val storage = EncryptedSharedPreferences.create(
        context,
        TOKEN_STORAGE,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAuthToken(token: String) {
        storage.edit { putString(TOKEN_KEY, token) }
    }

    fun getAuthToken(): String? {
        return storage.getString(TOKEN_KEY, null)
    }

    fun clearAuthToken() {
        storage.edit { remove(TOKEN_KEY) }
    }

    fun isTokenValid(): Boolean {
        val token = this.getAuthToken()
        return token != null
    }
}