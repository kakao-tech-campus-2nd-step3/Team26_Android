package org.ktc2.cokaen.wouldyouin.network

import android.content.SharedPreferences
import androidx.core.content.edit
import org.ktc2.cokaen.wouldyouin.data.model.SocialTokenResponse
import org.ktc2.cokaen.wouldyouin.data.model.TokenResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferenceManager @Inject constructor(
    private val prefs: SharedPreferences
) {
    companion object {
        private const val KEY_PHONE = "phone"
        private const val KEY_AREA = "area"
        private const val KEY_GENDER = "gender"
        private const val KEY_TOKEN = "token"
        private const val KEY_MEMBER_ID = "member_id"
        private const val KEY_MEMBER_TYPE = "member_type"
        private const val KEY_IS_WELCOME = "is_welcome"
    }

    var phone: String?
        get() = prefs.getString(KEY_PHONE, null)
        set(value) = prefs.edit { putString(KEY_PHONE, value) }

    var area: String?
        get() = prefs.getString(KEY_AREA, null)
        set(value) = prefs.edit { putString(KEY_AREA, value) }

    var gender: String?
        get() = prefs.getString(KEY_GENDER, null)
        set(value) = prefs.edit { putString(KEY_GENDER, value) }

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) = prefs.edit { putString(KEY_TOKEN, value) }

    var memberId: Long?
        get() = prefs.getLong(KEY_MEMBER_ID, -1).takeIf { it != -1L }
        set(value) = prefs.edit {
            if (value != null) putLong(KEY_MEMBER_ID, value)
            else remove(KEY_MEMBER_ID)
        }

    var memberType: String?
        get() = prefs.getString(KEY_MEMBER_TYPE, null)
        set(value) = prefs.edit { putString(KEY_MEMBER_TYPE, value) }

    var isWelcome: Boolean
        get() = prefs.getBoolean(KEY_IS_WELCOME, false)
        set(value) = prefs.edit { putBoolean(KEY_IS_WELCOME, value) }

    fun hasAdditionalInfo(): Boolean {
        return !phone.isNullOrEmpty() &&
                !area.isNullOrEmpty() &&
                !gender.isNullOrEmpty()
    }

    fun saveSocialTokenResponse(response: SocialTokenResponse) {
        token = response.token
        memberId = response.memberId
        memberType = response.memberType
        isWelcome = response.isWelcomeMember
    }

    fun saveTokenResponse(response: TokenResponse) {
        token = response.token
        memberId = response.memberId
        memberType = response.memberType.toString()
        isWelcome = false  // 추가 정보 등록 후에는 더 이상 welcome 상태가 아님
    }

    fun isNeedOnboarding(): Boolean {
        val token = prefs.getString(KEY_TOKEN, null)
        val memberType = prefs.getString(KEY_MEMBER_TYPE, null)
        return token != null && memberType == "welcome"
    }

    fun isAuthenticated(): Boolean {
        return !prefs.getString(KEY_TOKEN, null).isNullOrEmpty()
    }

    fun clearAll() {
        prefs.edit { clear() }
    }
}
