package com.eganathan.localizationsample

import android.app.Activity
import android.app.LocaleManager
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.LocaleListCompat
import com.eganathan.localizationsample.ui.theme.LocalizationSampleTheme
import kotlinx.coroutines.Dispatchers
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalizationSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    LangaugeSelectionComponent()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LangaugeSelectionComponent() {
    val context = LocalContext.current
    val deviceLocale = context.resources.configuration.locales.get(0)

    val currentLocale = remember { mutableStateOf(deviceLocale.toLanguageTag()) }

    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
    AppCompatDelegate.setApplicationLocales(appLocale)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.hello_world),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.heightIn(50.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                currentLocale.value = Locale("en").toLanguageTag()
                localeSelection(context = context, localeTag = currentLocale.value)
            }) {
                Text(text = stringResource(id = R.string.english))
            }

            Button(onClick = {
                currentLocale.value = Locale("ta").toLanguageTag()
                localeSelection(context = context, localeTag = currentLocale.value)
            }) {
                Text(text = stringResource(id = R.string.tamil))
            }

            Button(onClick = {
                currentLocale.value = Locale("hi").toLanguageTag()
                localeSelection(context = context, localeTag = currentLocale.value)
            }) {
                Text(text = stringResource(id = R.string.hindi))
            }

            Button(onClick = {
                currentLocale.value = Locale("ml").toLanguageTag()
                localeSelection(context = context, localeTag = currentLocale.value)
            }) {
                Text(text = stringResource(id = R.string.malayalam))
            }
        }
    }
}

object constants {
    val availableLang: List<String> = listOf("en", "ta", "hi")
}

fun localeSelection(context: Context, localeTag: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags(localeTag)
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(localeTag)
        )
    }
}
