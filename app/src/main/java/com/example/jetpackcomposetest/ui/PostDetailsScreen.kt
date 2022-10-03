package com.example.jetpackcomposetest.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetpackcomposetest.R
import com.example.jetpackcomposetest.common.Constants
import com.example.jetpackcomposetest.common.Texts
import com.example.jetpackcomposetest.flickrresponse.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "PostDetailsScreen"
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PostDetailsScreen(photo: Photo, navController: NavController) {



    val moods = listOf(
        R.string.dark_mood,
        R.string.national_day_mood,
        R.string.light_mood
    )

    val moodBtnText = remember {
        mutableStateOf(moods[Constants.isDarkMode.value])
    }

    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    val topAppBarText = remember { mutableStateOf("") }
    val changeLanguageBtnText = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    fun updateCurrentTexts(){
        coroutineScope.launch {
            Texts.updateText(context = context, resource = R.string.change_language).collect{
                changeLanguageBtnText.value = it
            }
        }
        coroutineScope.launch {
            Texts.updateText(context = context, resource = R.string.post_details).collect{
                topAppBarText.value = it
            }
        }
        coroutineScope.launch {
            Texts.updateText(context = context, resource = moods[Constants.isDarkMode.value]).collect()
        }
    }

    updateCurrentTexts()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        TopApplicationBar(
            title = topAppBarText.value,
            navController = navController
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    val currentLanguage = Locale.current.language == "ar"
                    setLanguage(
                        isArabic = !currentLanguage,
                        configuration = configuration,
                        context.resources
                    )
                    updateCurrentTexts()
                },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = changeLanguageBtnText.value)
            }

            Button(
                onClick = {
                    if (Constants.isDarkMode.value == 2) {
                        Constants.isDarkMode.value = 0
                    }else{
                        Constants.isDarkMode.value++
                    }
                    moodBtnText.value = moods[Constants.isDarkMode.value]
                },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = stringResource(moodBtnText.value))
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Row {
                ProfileImage(url = photo.url_s)
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = photo.title)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = photo.datetaken)
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(text = photo.description._content)
                Spacer(modifier = Modifier.size(12.dp))
                ContentImage(url = photo.url_s, navController)
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "")
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "")
                Icon(imageVector = Icons.Default.Share, contentDescription = "")
                Icon(imageVector = Icons.Default.Warning, contentDescription = "")
            }
        }
    }
}

