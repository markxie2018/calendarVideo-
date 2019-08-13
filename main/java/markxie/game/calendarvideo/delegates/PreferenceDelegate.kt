package markxie.game.calendarvideo.delegates

import android.content.Context
import android.content.SharedPreferences
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


@Suppress("UNCHECKED_CAST")
class PreferenceDelegate<T>(
    private val context: Context,
    private val default: T,
    private val prefName: String = "default"
) : ReadWriteProperty<Any?, T> {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }


    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getPreference(key = prefName)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(key = prefName, value = value)
    }

    private fun getPreference(key: String): T {
        return when (default) {
            is String -> prefs.getString(key, default)
            is Long -> prefs.getLong(key, default)
            is Boolean -> prefs.getBoolean(key, default)
            is Float -> prefs.getFloat(key, default)
            is Int -> prefs.getInt(key, default)
            else -> throw  IllegalArgumentException("Unknown Type.")
        } as T
    }

    private fun putPreference(key: String, value: T) = with(prefs.edit()) {
        when (value) {
            is String -> putString(key, value)
            is Long -> putLong(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            else -> throw IllegalArgumentException("Unknown Type.")
        }
    }.apply()
}