package com.eganathan.localizationsample

import android.app.LocaleManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale


sealed class Language {

    //common properties
    abstract val code: String
    abstract val titleRes: Int

    companion object {

        //Make sure you add all the languages here
        val allowedLocales = listOf(English, Tamil, Hindi, Malayalam, French)

        fun setLocale(context: Context, localeCode: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.getSystemService(LocaleManager::class.java).applicationLocales =
                    LocaleList.forLanguageTags(localeCode)
            } else {
                saveToLocalSharedAndUpdateResources(context = context, localeTag = localeCode)
            }
        }

        internal fun getCurrentLanguage(context: Context): Language {
            return this.allowedLocales.find { it.code == getCurrentLocale(context) } ?: English
        }

        private fun getCurrentLocale(context: Context): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.getSystemService(LocaleManager::class.java).applicationLocales.toLanguageTags()
            } else {
                AppCompatDelegate.getApplicationLocales().toLanguageTags()
            }
        }


        // REQUIRED FOR API LEVEL 32 AND LOWER AND/OR FOR WITHOUT ACTIVITY RE-CREATION
        private const val sharedPrefSelectedLocaleKey = "LocalePreference"
        private fun getLocaleSharedPreference(context: Context): SharedPreferences? {
            return context.applicationContext?.getSharedPreferences(
                sharedPrefSelectedLocaleKey,
                Context.MODE_PRIVATE
            )
        }

        private fun setLocaleForDevicesLowerThanTiramisu(localeTag: String, context: Context) {
            val locale = Locale(localeTag)
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }

        private fun saveToLocalSharedAndUpdateResources(context: Context, localeTag: String) {
            val sharedPref = getLocaleSharedPreference(context) ?: return

            with(sharedPref.edit()) {
                putString(sharedPrefSelectedLocaleKey, localeTag)
                apply()
            }
            setLocaleForDevicesLowerThanTiramisu(localeTag, context)
        }

        //This needs to be called OnStart() or OnCreation() of the Activity for Android API level 32 and below
        fun configureLocaleOnStartForDevicesLowerThanTiramisu(context: Context) {
            setLocaleForDevicesLowerThanTiramisu(
                context = context,
                localeTag = getLocaleSharedPreference(context)?.getString(
                    sharedPrefSelectedLocaleKey,
                    English.code
                ) ?: English.code
            )
        }
    }

    object English : Language() {
        override val code: String = "en"
        override val titleRes: Int = R.string.english
    }

    object Tamil : Language() {
        override val code: String = "ta"
        override val titleRes: Int = R.string.tamil
    }

    object Malayalam : Language() {
        override val code: String = "ml"
        override val titleRes: Int = R.string.malayalam
    }

    object Hindi : Language() {
        override val code: String = "hi"
        override val titleRes: Int = R.string.hindi
    }

    object French : Language() {
        override val code: String = "fr"
        override val titleRes: Int = R.string.french
    }

    object Arabic : Language() {
        override val code: String = "ar"
        override val titleRes: Int = R.string.french //todo
    }

}